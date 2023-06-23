package com.jaehyun.reservation.user.user.controller;

import com.jaehyun.reservation.global.exception.DuplicatedIdOrPhoneNumException;
import com.jaehyun.reservation.user.user.domain.dto.UserJoinDto;
import com.jaehyun.reservation.user.user.domain.dto.UserLoginDto;
import com.jaehyun.reservation.user.user.domain.entity.User;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserController {

  private final UserService userService;

  // 회원가입
  @PostMapping("/user/join")
  public ResponseEntity<String> join(@RequestBody UserJoinDto userJoinDto) throws DuplicatedIdOrPhoneNumException {
    userService.join(userJoinDto);
    return ResponseEntity.status(HttpStatus.OK).body("join ok");
  }

  @GetMapping("/user/login")
  public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto) {
    String token = userService.login(userLoginDto);
    return ResponseEntity.status(HttpStatus.OK).body(token);
  }

  @DeleteMapping("/user/reservation/quit")
  public ResponseEntity<String>  quit(Principal principal) {
    userService.quit(principal);
    return ResponseEntity.status(HttpStatus.OK).body("quit ok");
  }
}
