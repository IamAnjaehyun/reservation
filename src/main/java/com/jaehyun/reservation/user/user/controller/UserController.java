package com.jaehyun.reservation.user.user.controller;

import com.jaehyun.reservation.global.common.APIResponse;
import com.jaehyun.reservation.global.config.JwtTokenProvider;
import com.jaehyun.reservation.global.exception.impl.user.DuplicatedIdOrPhoneNumException;
import com.jaehyun.reservation.user.user.domain.dto.UserJoinDto;
import com.jaehyun.reservation.user.user.domain.dto.UserLoginDto;
import com.jaehyun.reservation.user.user.service.UserService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserController {

  private final UserService userService;

  // 회원가입
  @PostMapping("/user/join")
  public APIResponse<String> join(@RequestBody UserJoinDto userJoinDto)
      throws DuplicatedIdOrPhoneNumException {
    return userService.join(userJoinDto);
  }

  @GetMapping("/user/login")
  public APIResponse<String> login(@RequestBody UserLoginDto userLoginDto) {
    return userService.login(userLoginDto);
  }

  @DeleteMapping("/user/reservation/quit")
  public APIResponse<Void> quit(Principal principal) {
    return userService.quit(principal);
  }
}
