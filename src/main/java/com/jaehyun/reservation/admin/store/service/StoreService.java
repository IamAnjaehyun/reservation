package com.jaehyun.reservation.admin.store.service;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

  private final UserService userService;
  private final StoreRepository storeRepository;

  public StoreResDto createStore(StoreReqDto storeReqDto, Principal principal) {
    User admin = userService.findUserByPrincipal(principal);
    //중복된 상점의 이름이 들어온다면 예외처리
    if (storeRepository.existsByName(storeReqDto.getName())) {
      throw new AlreadyExistStoreException();
    }

    //권한체크 안해도됨 security에서 하기떄문
    Store store = Store.builder()
        .user(admin)
        .name(storeReqDto.getName())
        .phoneNum(storeReqDto.getPhoneNum())
        .description(storeReqDto.getDescription())
        .location(storeReqDto.getLocation())
        .totalRating(0L)
        .averageRating(0L)
        .build();
    storeRepository.save(store);

    return StoreResDto.fromStore(store);

  }

  public StoreResDto updateStore(Long storeId, StoreReqDto storeDto, Principal principal) {
    //상점 중복이름 불가
    if (storeRepository.existsByName(storeDto.getName())) {
      throw new AlreadyExistStoreException();
    }

    User admin = userService.findUserByPrincipal(principal);
    Store store = storeRepository.findById(storeId).orElseThrow(NotExistStoreException::new);

    User user = store.getUser();

    if (user != null && user.getId() != null && user.getId().equals(admin.getId())) {
      store.setName(storeDto.getName());
      store.setDescription(storeDto.getDescription());
      store.setLocation(storeDto.getLocation());
      store.setPhoneNum(storeDto.getPhoneNum());

      storeRepository.save(store);
      return StoreResDto.fromStore(store);
    } else {
      throw new UnauthorizedException();
    }
  }

  public void deleteStore(Long storeId, Principal principal) {
    User admin = userService.findUserByPrincipal(principal);
    Store store = storeRepository.findById(storeId).orElseThrow(NotExistStoreException::new);

    Long storeAdminId = store.getUser().getId(); // Assuming this is the ID of the store admin

    if (storeAdminId != null && storeAdminId.equals(admin.getId())) {
      storeRepository.deleteById(store.getId());
    } else {
      throw new UnauthorizedException();
    }
  }


  public Page<StoreViewDto> getStoreList(Pageable pageable) {
    Page<Store> storePage = storeRepository.findAll(pageable);
    return storePage.map(StoreViewDto::fromStore);
  }

  public StoreViewDto getStoreDetail(Long storeId) {

    // 상점 상세 조회
    Store store = storeRepository.findById(storeId)
        .orElseThrow(NotExistStoreException::new);
    return StoreViewDto.fromStore(store);
  }
}
