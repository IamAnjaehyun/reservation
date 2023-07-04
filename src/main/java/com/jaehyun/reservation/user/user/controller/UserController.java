package com.jaehyun.reservation.user.user.controller;

import com.jaehyun.reservation.global.common.APIResponse;
import com.jaehyun.reservation.user.user.service.UserService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reservation/user")
public class UserController {

  private final UserService userService;
  private static final String API_NAME = "user";

  @DeleteMapping("/quit")
  public APIResponse<Void> quit(Principal principal) {
    userService.quit(principal);
    return APIResponse.delete();
  }

  @PostMapping("/changeRoleAdmin")
  public APIResponse<String> changeRole(Principal principal) {
    return APIResponse.success(API_NAME, userService.changeRole(principal));
  }
}
