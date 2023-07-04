package com.jaehyun.reservation.admin.store.service;

import com.jaehyun.reservation.admin.store.domain.dto.StoreReqDto;
import com.jaehyun.reservation.admin.store.domain.dto.StoreResDto;
import com.jaehyun.reservation.admin.store.domain.dto.StoreViewDto;
import com.jaehyun.reservation.admin.store.domain.entity.Store;
import com.jaehyun.reservation.admin.store.domain.repository.StoreRepository;
import com.jaehyun.reservation.global.common.APIResponse;
import com.jaehyun.reservation.global.exception.impl.role.UnauthorizedException;
import com.jaehyun.reservation.global.exception.impl.store.AlreadyExistStoreException;
import com.jaehyun.reservation.global.exception.impl.store.NotExistStoreException;
import com.jaehyun.reservation.global.exception.impl.user.NotExistUserException;
import com.jaehyun.reservation.user.user.domain.entity.User;
import com.jaehyun.reservation.user.user.domain.repository.UserRepository;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

  private final UserRepository userRepository;
  private final StoreRepository storeRepository;
  private final String API_NAME = "store";

  public APIResponse<StoreResDto> createStore(StoreReqDto storeReqDto, Principal principal) {
    Optional<User> adminOptional = Optional.ofNullable(
        userRepository.findByLoginId(principal.getName()).orElseThrow(NotExistUserException::new));
    //중복된 상점의 이름이 들어온다면 예외처리
    Optional<Store> storeOptional = Optional.ofNullable(
        storeRepository.findByName(storeReqDto.getName())
            .orElseThrow(AlreadyExistStoreException::new));

    //권한체크 안해도됨 security에서 하기떄문
    Store store = Store.builder()
        .user(adminOptional.get())
        .name(storeReqDto.getName())
        .phoneNum(storeReqDto.getPhoneNum())
        .description(storeReqDto.getDescription())
        .location(storeReqDto.getLocation())
        .totalRating(0L)
        .averageRating(0L)
        .build();
    storeRepository.save(store);

    StoreResDto storeResDto = StoreResDto.builder()
        .adminName(store.getUser().getName())
        .name(store.getName())
        .description(store.getDescription())
        .location(store.getLocation())
        .phoneNum(store.getPhoneNum())
        .build();
    return APIResponse.success(API_NAME, storeResDto);
  }

  public APIResponse<StoreResDto> updateStore(Long storeId, StoreReqDto storeDto,
      Principal principal) {
    //상점 중복이름 불가
    if (storeRepository.existsByName(storeDto.getName())) {
      throw new AlreadyExistStoreException();
    }
    Optional<User> adminOptional = Optional.ofNullable(
        userRepository.findByLoginId(principal.getName()).orElseThrow(
            NotExistUserException::new));
    Optional<Store> storeOptional = Optional.ofNullable(
        storeRepository.findById(storeId).orElseThrow(NotExistStoreException::new));

    Store store = storeOptional.get();
    User user = store.getUser();

    if (user.getId().equals(adminOptional.get().getId())) {
      store.setName(storeDto.getName());
      store.setDescription(storeDto.getDescription());
      store.setLocation(storeDto.getLocation());
      store.setPhoneNum(storeDto.getPhoneNum());

      storeRepository.save(store);
      StoreResDto storeResDto = StoreResDto.builder()
          .adminName(store.getUser().getName())
          .name(store.getName())
          .description(store.getDescription())
          .location(store.getLocation())
          .phoneNum(store.getPhoneNum())
          .build();
      return APIResponse.success(API_NAME, storeResDto);
    } else {
      throw new UnauthorizedException();
    }
  }

  public APIResponse<String> deleteStore(Long storeId, Principal principal) {
    Optional<User> adminOptional = userRepository.findByLoginId(principal.getName());
    Optional<Store> storeOptional = Optional.ofNullable(
        storeRepository.findById(storeId).orElseThrow(NotExistStoreException::new));

      Store store = storeOptional.get();
      User user = store.getUser();

      if (user.getId().equals(adminOptional.get().getId())) {
        storeRepository.deleteById(store.getId());
        return APIResponse.delete();
      } else {
        throw new UnauthorizedException();
      }
  }

  public APIResponse<Page<StoreViewDto>> getStoreList(Pageable pageable) {
    List<StoreViewDto> storeList = new ArrayList<>();

    // 상점 목록 조회
    Page<Store> storePage = storeRepository.findAll(pageable);

    for (Store store : storePage.getContent()) {
      StoreViewDto storeDto = StoreViewDto.builder()
          .storeId(store.getId())
          .name(store.getName())
          .description(store.getDescription())
          .location(store.getLocation())
          .phoneNum(store.getPhoneNum())
          .averageRating(store.getAverageRating())
          .totalReviewCount(store.getTotalReviewCount())
          .build();

      storeList.add(storeDto);
    }
    return APIResponse.success(API_NAME, new PageImpl<>(storeList, pageable, storePage.getTotalElements()));
  }

  public APIResponse<StoreViewDto> getStoreDetail(Long storeId) {

    // 상점 목록 조회
    Optional<Store> storeOptional = Optional.ofNullable(
        storeRepository.findById(storeId).orElseThrow(NotExistStoreException::new));
    Store store = storeOptional.get();

    StoreViewDto storeDto = StoreViewDto.builder()
        .storeId(store.getId())
        .name(store.getName())
        .description(store.getDescription())
        .location(store.getLocation())
        .phoneNum(store.getPhoneNum())
        .averageRating(store.getAverageRating())
        .totalReviewCount(store.getTotalReviewCount())
        .build();
    return APIResponse.success(API_NAME, storeDto);
  }
}
