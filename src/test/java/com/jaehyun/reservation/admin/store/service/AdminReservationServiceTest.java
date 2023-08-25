package com.jaehyun.reservation.admin.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jaehyun.reservation.admin.store.domain.entity.Store;
import com.jaehyun.reservation.admin.store.domain.repository.StoreRepository;
import com.jaehyun.reservation.user.reservation.domain.dto.ReservationResDto;
import com.jaehyun.reservation.user.reservation.domain.entity.Reservation;
import com.jaehyun.reservation.user.reservation.domain.repository.ReservationRepository;
import com.jaehyun.reservation.user.type.ReservationStatus;
import com.jaehyun.reservation.user.user.domain.entity.User;
import com.jaehyun.reservation.user.user.domain.repository.UserRepository;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
public class AdminReservationServiceTest {

  @InjectMocks
  private AdminReservationService adminReservationService;

  @Mock
  private ReservationRepository reservationRepository;

  @Mock
  private StoreRepository storeRepository;

  @Mock
  private UserRepository userRepository;

  @Test
  public void testGetAllStoreReservationList() {
    // Given
    Principal principal = mock(Principal.class);
    User user = new User();
    when(userRepository.findByLoginId(principal.getName())).thenReturn(Optional.of(user));

    Store store1 = new Store();
    store1.setId(1L);
    Store store2 = new Store();
    store2.setId(2L);
    List<Store> storeList = Arrays.asList(store1, store2);
    when(storeRepository.findAllByUser(user)).thenReturn(storeList);

    Reservation reservation1 = new Reservation();
    reservation1.setId(101L);
    reservation1.setStatus(ReservationStatus.REQUEST);
    reservation1.setStore(store1);
    reservation1.setUser(user);
    reservation1.setReservationDateTime(LocalDateTime.now());
    reservation1.setReservationPeopleNum(3);

    Reservation reservation2 = new Reservation();
    reservation2.setId(102L);
    reservation2.setStatus(ReservationStatus.OKAY);
    reservation2.setStore(store2);
    reservation2.setUser(user);
    reservation2.setReservationDateTime(LocalDateTime.now());
    reservation2.setReservationPeopleNum(2);

    Page<Reservation> reservationPage = new PageImpl<>(Arrays.asList(reservation1, reservation2));
    when(reservationRepository.findAllByStoreIn(eq(storeList), any(Pageable.class))).thenReturn(
        reservationPage);

    // When
    Page<ReservationResDto> result = adminReservationService.getAllStoreReservationList(principal,
        Pageable.unpaged());

    // Then
    assertEquals(2, result.getTotalElements());
    ReservationResDto resultDto1 = result.getContent().get(0);
    assertEquals(101L, resultDto1.getReservationId());
    assertEquals(1L, resultDto1.getStoreId());
    assertEquals(ReservationStatus.REQUEST, resultDto1.getReservationStatus());
    assertNotNull(resultDto1.getReservationDateTime());
    assertEquals(3, resultDto1.getReservationPeopleNum());

    ReservationResDto resultDto2 = result.getContent().get(1);
    assertEquals(102L, resultDto2.getReservationId());
    assertEquals(2L, resultDto2.getStoreId());
    assertEquals(ReservationStatus.OKAY, resultDto2.getReservationStatus());
    assertNotNull(resultDto2.getReservationDateTime());
    assertEquals(2, resultDto2.getReservationPeopleNum());

    verify(userRepository, times(1)).findByLoginId(any());
    verify(storeRepository, times(1)).findAllByUser(user);
    verify(reservationRepository, times(1)).findAllByStoreIn(eq(storeList), any(Pageable.class));
  }

  @Test
  public void testGetStoreReservationListByStatus() {
    // Given
    Long storeId = 1L;
    ReservationStatus status = ReservationStatus.OKAY;
    Principal principal = mock(Principal.class);
    User user = new User();
    when(userRepository.findByLoginId(principal.getName())).thenReturn(Optional.of(user));

    Store store = new Store();
    store.setId(storeId);
    when(storeRepository.findByUserAndId(user, storeId)).thenReturn(Optional.of(store));

    Reservation reservation1 = new Reservation();
    reservation1.setId(101L);
    reservation1.setStatus(ReservationStatus.OKAY);
    reservation1.setStore(store);
    reservation1.setUser(user);

    Reservation reservation2 = new Reservation();
    reservation2.setId(102L);
    reservation2.setStatus(ReservationStatus.OKAY);
    reservation2.setStore(store);
    reservation2.setUser(user);

    Page<Reservation> reservationPage = new PageImpl<>(Arrays.asList(reservation1, reservation2));
    when(reservationRepository.findAllByStoreAndStatus(eq(store), eq(status),
        any(Pageable.class))).thenReturn(reservationPage);

    // When
    Page<ReservationResDto> result = adminReservationService.getStoreReservationListByStatus(
        storeId, status, principal, Pageable.unpaged());

    // Then
    assertEquals(2, result.getTotalElements());
    ReservationResDto resultDto1 = result.getContent().get(0);
    assertEquals(101L, resultDto1.getReservationId());
    assertEquals(ReservationStatus.OKAY, resultDto1.getReservationStatus());

    ReservationResDto resultDto2 = result.getContent().get(1);
    assertEquals(102L, resultDto2.getReservationId());
    assertEquals(ReservationStatus.OKAY, resultDto2.getReservationStatus());

    verify(userRepository, times(1)).findByLoginId(any());
    verify(storeRepository, times(1)).findByUserAndId(user, storeId);
    verify(reservationRepository, times(1)).findAllByStoreAndStatus(eq(store), eq(status),
        any(Pageable.class));
  }

  @Test
  public void testGetStoreReservationListByDateAndStatus() {
    // Given
    Long storeId = 1L;
    LocalDate localDate = LocalDate.now();
    ReservationStatus status = ReservationStatus.OKAY;
    Principal principal = mock(Principal.class);
    User user = new User();
    when(userRepository.findByLoginId(principal.getName())).thenReturn(Optional.of(user));

    Store store = new Store();
    store.setId(storeId);
    when(storeRepository.findByUserAndId(user, storeId)).thenReturn(Optional.of(store));

    LocalDateTime startDate = localDate.atStartOfDay();
    LocalDateTime endDate = localDate.atTime(LocalTime.MAX);

    Reservation reservation1 = new Reservation();
    reservation1.setId(101L);
    reservation1.setStatus(ReservationStatus.OKAY);
    reservation1.setStore(store);
    reservation1.setUser(user);
    reservation1.setReservationDateTime(startDate.plusHours(1));  // 예약 시간: localDate + 01:00

    Reservation reservation2 = new Reservation();
    reservation2.setId(102L);
    reservation2.setStatus(ReservationStatus.OKAY);
    reservation2.setStore(store);
    reservation2.setUser(user);
    reservation2.setReservationDateTime(startDate.plusHours(2));  // 예약 시간: localDate + 02:00

    Page<Reservation> reservationPage = new PageImpl<>(Arrays.asList(reservation1, reservation2));
    when(reservationRepository.findAllByStoreAndStatusAndReservationDateTimeBetween(eq(store),
        eq(status), eq(startDate), eq(endDate), any(Pageable.class))).thenReturn(reservationPage);

    // When
    Page<ReservationResDto> result = adminReservationService.getStoreReservationListByDateAndStatus(
        storeId, localDate, status, principal, Pageable.unpaged());

    // Then
    assertEquals(2, result.getTotalElements());
    ReservationResDto resultDto1 = result.getContent().get(0);
    assertEquals(101L, resultDto1.getReservationId());
    assertEquals(ReservationStatus.OKAY, resultDto1.getReservationStatus());

    ReservationResDto resultDto2 = result.getContent().get(1);
    assertEquals(102L, resultDto2.getReservationId());
    assertEquals(ReservationStatus.OKAY, resultDto2.getReservationStatus());

    verify(userRepository, times(1)).findByLoginId(any());
    verify(storeRepository, times(1)).findByUserAndId(user, storeId);
    verify(reservationRepository, times(1)).findAllByStoreAndStatusAndReservationDateTimeBetween(
        eq(store), eq(status), eq(startDate), eq(endDate), any(Pageable.class));
  }
}
