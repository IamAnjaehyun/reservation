package com.jaehyun.reservation.admin.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jaehyun.reservation.admin.store.domain.dto.StoreReqDto;
import com.jaehyun.reservation.admin.store.domain.dto.StoreResDto;
import com.jaehyun.reservation.admin.store.domain.dto.StoreViewDto;
import com.jaehyun.reservation.admin.store.domain.entity.Store;
import com.jaehyun.reservation.admin.store.domain.repository.StoreRepository;
import com.jaehyun.reservation.global.exception.impl.role.UnauthorizedException;
import com.jaehyun.reservation.global.exception.impl.store.AlreadyExistStoreException;
import com.jaehyun.reservation.global.exception.impl.store.NotExistStoreException;
import com.jaehyun.reservation.user.user.domain.entity.User;
import com.jaehyun.reservation.user.user.service.UserService;
import java.security.Principal;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class StoreServiceTest {

  @Mock
  private UserService userService;

  @Mock
  private StoreRepository storeRepository;

  private StoreService storeService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    storeService = new StoreService(userService, storeRepository);
  }

  @Test
  public void testCreateStore_Success() {
    // Given
    Principal principal = mock(Principal.class);
    User admin = new User();
    when(userService.findUserByPrincipal(principal)).thenReturn(admin);
    when(storeRepository.existsByName(any())).thenReturn(false);

    StoreReqDto storeReqDto = new StoreReqDto();
    storeReqDto.setName("Test Store");
    storeReqDto.setPhoneNum("123-456-7890");
    storeReqDto.setDescription("Test description");
    storeReqDto.setLocation("Test location");

    // When
    StoreResDto storeResDto = storeService.createStore(storeReqDto, principal);

    // Then
    assertNotNull(storeResDto);
    assertEquals("Test Store", storeResDto.getName());
    assertEquals("123-456-7890", storeResDto.getPhoneNum());
    assertEquals("Test description", storeResDto.getDescription());
    assertEquals("Test location", storeResDto.getLocation());

    verify(storeRepository, times(1)).save(any());
  }

  @Test
  public void testCreateStore_AlreadyExists() {
    // Given
    Principal principal = mock(Principal.class);
    when(storeRepository.existsByName(any())).thenReturn(true);

    StoreReqDto storeReqDto = new StoreReqDto();
    storeReqDto.setName("Existing Store");

    // When, Then
    assertThrows(AlreadyExistStoreException.class,
        () -> storeService.createStore(storeReqDto, principal));
    verify(storeRepository, times(0)).save(any());
  }

  @Test
  public void testUpdateStore_Success() {
    // Given
    Principal principal = mock(Principal.class);
    User admin = new User();
    admin.setId(1L);
    when(userService.findUserByPrincipal(principal)).thenReturn(admin);
    when(storeRepository.existsByName(any())).thenReturn(false);

    Store store = new Store();
    store.setId(1L);
    store.setName("Old Store");
    store.setDescription("Old description");
    store.setLocation("Old location");
    store.setPhoneNum("123-456-7890");
    User storeUser = new User();
    storeUser.setId(1L);
    store.setUser(storeUser);
    when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

    StoreReqDto storeReqDto = new StoreReqDto();
    storeReqDto.setName("Updated Store");
    storeReqDto.setDescription("Updated description");
    storeReqDto.setLocation("Updated location");
    storeReqDto.setPhoneNum("987-654-3210");

    // When
    StoreResDto storeResDto = storeService.updateStore(1L, storeReqDto, principal);

    // Then
    assertNotNull(storeResDto);
    assertEquals("Updated Store", storeResDto.getName());
    assertEquals("987-654-3210", storeResDto.getPhoneNum());
    assertEquals("Updated description", storeResDto.getDescription());
    assertEquals("Updated location", storeResDto.getLocation());

    verify(storeRepository, times(1)).save(eq(store));
  }


  @Test
  public void testUpdateStore_AlreadyExists() {
    // Given
    Principal principal = mock(Principal.class);
    when(storeRepository.existsByName(any())).thenReturn(true);

    StoreReqDto storeReqDto = new StoreReqDto();
    storeReqDto.setName("Existing Store");

    // When, Then
    assertThrows(AlreadyExistStoreException.class,
        () -> storeService.updateStore(1L, storeReqDto, principal));
    verify(storeRepository, times(0)).save(any());
  }

  @Test
  public void testUpdateStore_Unauthorized() {
    // Given
    Principal principal = mock(Principal.class);
    User admin = new User();
    User otherUser = new User();
    otherUser.setId(999L);
    when(userService.findUserByPrincipal(principal)).thenReturn(otherUser);

    Store store = new Store();
    store.setId(1L);
    store.setUser(admin);

    when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

    StoreReqDto storeReqDto = new StoreReqDto();

    // When, Then
    assertThrows(UnauthorizedException.class,
        () -> storeService.updateStore(1L, storeReqDto, principal));
    verify(storeRepository, times(0)).save(any());
  }

  @Test
  public void testDeleteStore_Success() {
    // Given
    Principal principal = mock(Principal.class);
    User admin = new User();
    admin.setId(1L);
    when(userService.findUserByPrincipal(principal)).thenReturn(admin);

    Store store = new Store();
    store.setId(1L);
    User storeUser = new User();
    storeUser.setId(1L);
    store.setUser(storeUser);

    when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

    // When
    storeService.deleteStore(1L, principal);

    // Then
    verify(storeRepository, times(1)).deleteById(1L);
  }

  @Test
  public void testDeleteStore_Unauthorized() {
    // Given
    Principal principal = mock(Principal.class);
    User admin = new User();
    User otherUser = new User();
    otherUser.setId(999L);
    when(userService.findUserByPrincipal(principal)).thenReturn(otherUser);

    Store store = new Store();
    store.setId(1L);
    store.setUser(admin);

    when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

    // When, Then
    assertThrows(UnauthorizedException.class, () -> storeService.deleteStore(1L, principal));
    verify(storeRepository, times(0)).deleteById(any());
  }

  @Test
  public void testGetStoreList() {
    // Given
    Pageable pageable = mock(Pageable.class);
    Store store = new Store();
    store.setId(1L);
    store.setName("Test Store");
    store.setDescription("Test description");
    store.setLocation("Test location");
    store.setPhoneNum("123-456-7890");
    Page<Store> storePage = new PageImpl<>(Collections.singletonList(store));

    when(storeRepository.findAll(pageable)).thenReturn(storePage);

    // When
    Page<StoreViewDto> storeViewDtoPage = storeService.getStoreList(pageable);

    // Then
    assertNotNull(storeViewDtoPage);
    assertEquals(1, storeViewDtoPage.getTotalElements());
    StoreViewDto storeViewDto = storeViewDtoPage.getContent().get(0);
    assertEquals(1L, storeViewDto.getStoreId());
    assertEquals("Test Store", storeViewDto.getName());
    assertEquals("Test description", storeViewDto.getDescription());
    assertEquals("Test location", storeViewDto.getLocation());
    assertEquals("123-456-7890", storeViewDto.getPhoneNum());
  }

  @Test
  public void testGetStoreDetail_Success() {
    // Given
    Store store = new Store();
    store.setId(1L);
    store.setName("Test Store");
    store.setDescription("Test description");
    store.setLocation("Test location");
    store.setPhoneNum("123-456-7890");
    when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

    // When
    StoreViewDto storeViewDto = storeService.getStoreDetail(1L);

    // Then
    assertNotNull(storeViewDto);
    assertEquals(1L, storeViewDto.getStoreId());
    assertEquals("Test Store", storeViewDto.getName());
    assertEquals("Test description", storeViewDto.getDescription());
    assertEquals("Test location", storeViewDto.getLocation());
    assertEquals("123-456-7890", storeViewDto.getPhoneNum());
  }

  @Test
  public void testGetStoreDetail_NotExist() {
    // Given
    when(storeRepository.findById(1L)).thenReturn(Optional.empty());

    // When, Then
    assertThrows(NotExistStoreException.class, () -> storeService.getStoreDetail(1L));
  }
}
