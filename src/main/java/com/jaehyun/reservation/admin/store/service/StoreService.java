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
import com.jaehyun.reservation.user.user.domain.entity.User;
import com.jaehyun.reservation.user.user.domain.repository.UserRepository;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

  private final UserRepository userRepository;
  private final StoreRepository storeRepository;
  private final String API_NAME = "store";

  public APIResponse<StoreResDto> createStore(StoreReqDto storeReqDto, Principal principal) {
    Optional<User> adminOptional = userRepository.findByLoginId(principal.getName());
    log.info(storeReqDto.getName());
    if (!storeRepository.existsByName(storeReqDto.getName())) {
      //권한체크 안해도됨 security에서 하기떄문
      log.info("if문 안으로 들어옴");
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
      log.info(store.getName());

      StoreResDto storeResDto = StoreResDto.builder()
          .adminName(store.getUser().getName())
          .name(store.getName())
          .description(store.getDescription())
          .location(store.getLocation())
          .phoneNum(store.getPhoneNum())
          .build();
      return APIResponse.success(API_NAME, storeResDto);
    } else {
      throw new AlreadyExistStoreException();
    }
  }

  public APIResponse<StoreResDto> updateStore(String storeId, StoreReqDto storeDto,
      Principal principal) {
    if (storeRepository.existsByName(storeDto.getName())) {
      throw new AlreadyExistStoreException();
    }
    Optional<User> adminOptional = userRepository.findByLoginId(principal.getName());
    Optional<Store> storeOptional = storeRepository.findByName(storeId);

    if (storeOptional.isPresent()) {
      Store store = storeOptional.get();
      User user = store.getUser();

      if (user.getId().equals(adminOptional.get().getId())) {
        // 기존 상점 정보 가져오기
        String name = store.getName();
        String description = store.getDescription();
        String location = store.getLocation();
        String phoneNum = store.getPhoneNum();

        // StoreDto에서 받은 정보로 필드 업데이트
        if (storeDto.getName() != null) {
          name = storeDto.getName();
        }
        if (storeDto.getDescription() != null) {
          description = storeDto.getDescription();
        }
        if (storeDto.getLocation() != null) {
          location = storeDto.getLocation();
        }
        if (storeDto.getPhoneNum() != null) {
          phoneNum = storeDto.getPhoneNum();
        }

        Store updatedStore = Store.builder()
            .id(store.getId())
            .user(store.getUser())
            .name(name)
            .description(description)
            .location(location)
            .phoneNum(phoneNum)
            .averageRating(store.getAverageRating())
            .totalRating(store.getTotalRating())
            .totalReviewCount(store.getTotalReviewCount())
            .build();

        storeRepository.save(updatedStore);
        StoreResDto storeResDto = StoreResDto.builder()
            .adminName(updatedStore.getUser().getName())
            .name(updatedStore.getName())
            .description(updatedStore.getDescription())
            .location(updatedStore.getLocation())
            .phoneNum(updatedStore.getPhoneNum())
            .build();
        return APIResponse.success(API_NAME, storeResDto);
      } else {
        throw new UnauthorizedException();
      }
    } else {
      throw new NotExistStoreException();
    }
  }

  public APIResponse<String> deleteStore(String storeId, Principal principal) {
    Optional<User> adminOptional = userRepository.findByLoginId(principal.getName());
    Optional<Store> storeOptional = storeRepository.findByName(storeId);

    if (storeOptional.isPresent()) {
      Store store = storeOptional.get();
      User user = store.getUser();

      if (user.getId().equals(adminOptional.get().getId())) {
        storeRepository.deleteById(store.getId());
        return APIResponse.delete();
      } else {
        throw new UnauthorizedException();
      }
    } else {
      throw new NotExistStoreException();
    }
  }

  public APIResponse<List<StoreViewDto>> getStoreList(){
    List<StoreViewDto> storeList = new ArrayList<>();

    // 상점 목록 조회
    List<Store> stores = storeRepository.findAll();

    for (Store store : stores) {
      StoreViewDto storeDto = StoreViewDto.builder()
          .name(store.getName())
          .description(store.getDescription())
          .location(store.getLocation())
          .phoneNum(store.getPhoneNum())
          .averageRating(store.getAverageRating())
          .totalReviewCount(store.getTotalReviewCount())
          .build();

      storeList.add(storeDto);
    }
    return APIResponse.success(API_NAME, storeList);
  }

  public APIResponse<StoreViewDto> getStoreDetail(String storeName){

    // 상점 목록 조회
    Optional<Store> storeOptional = Optional.ofNullable(
        storeRepository.findByName(storeName).orElseThrow(NotExistStoreException::new));
    Store store = storeOptional.get();

      StoreViewDto storeDto = StoreViewDto.builder()
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
