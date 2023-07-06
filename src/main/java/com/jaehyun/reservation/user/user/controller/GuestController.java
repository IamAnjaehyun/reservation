package com.jaehyun.reservation.user.user.controller;

import com.jaehyun.reservation.admin.store.domain.dto.StoreViewDto;
import com.jaehyun.reservation.admin.store.service.StoreService;
import com.jaehyun.reservation.global.common.APIResponse;
import com.jaehyun.reservation.global.exception.impl.user.DuplicatedIdOrPhoneNumException;
import com.jaehyun.reservation.user.user.domain.dto.UserJoinDto;
import com.jaehyun.reservation.user.user.domain.dto.UserLoginDto;
import com.jaehyun.reservation.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/guest")
public class GuestController {

  private final UserService userService;
  private final StoreService storeService;

  // 회원가입
  @PostMapping("/join")
  public APIResponse<String> join(@RequestBody UserJoinDto userJoinDto)
      throws DuplicatedIdOrPhoneNumException {
    return APIResponse.success("user", userService.join(userJoinDto));
  }

  @GetMapping("/login")
  public APIResponse<String> login(@RequestBody UserLoginDto userLoginDto) {
    return APIResponse.success("accessToken", userService.login(userLoginDto));
  }

  //상점 조회
  @GetMapping("/store")
  public APIResponse<Page<StoreViewDto>> storeList(Pageable pageable) {
    return APIResponse.success("storeList", storeService.getStoreList(pageable));
  }

  //상점 상세 조회
  @GetMapping("/store/{storeId}")
  public APIResponse<StoreViewDto> storeDetail(@PathVariable Long storeId) {
    return APIResponse.success("store", storeService.getStoreDetail(storeId));
  }
}
