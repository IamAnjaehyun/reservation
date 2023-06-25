package com.jaehyun.reservation.admin.store.service;

import com.jaehyun.reservation.admin.store.domain.dto.StoreReqDto;
import com.jaehyun.reservation.admin.store.domain.dto.StoreResDto;
import com.jaehyun.reservation.admin.store.domain.entity.Store;
import com.jaehyun.reservation.admin.store.domain.repository.StoreRepository;
import com.jaehyun.reservation.global.common.APIResponse;
import com.jaehyun.reservation.global.exception.impl.store.AlreadyExistStoreException;
import com.jaehyun.reservation.user.user.domain.entity.User;
import com.jaehyun.reservation.user.user.domain.repository.UserRepository;
import java.security.Principal;
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
}
