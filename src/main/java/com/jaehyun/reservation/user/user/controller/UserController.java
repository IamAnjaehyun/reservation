package com.jaehyun.reservation.user.user.controller;

import com.jaehyun.reservation.global.exception.DuplicatedIdOrPhoneNumException;
import com.jaehyun.reservation.user.user.domain.dto.UserJoinDto;
import com.jaehyun.reservation.user.user.domain.dto.UserLoginDto;
import com.jaehyun.reservation.user.user.service.UserService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation/v1/user")
public class UserController {

  private final UserService userService;

  // 회원가입
  @PostMapping("/join")
  public String join(@RequestBody UserJoinDto userJoinDto) throws DuplicatedIdOrPhoneNumException {
    return userService.join(userJoinDto);
  }

  @GetMapping("/login")
  public String login(@RequestBody UserLoginDto userLoginDto) {
    return userService.login(userLoginDto);
  }

  @DeleteMapping("/quit")
  public String quit(@RequestParam String phoneNum, Principal principal) {
    return userService.quit(phoneNum, principal);
  }
}
