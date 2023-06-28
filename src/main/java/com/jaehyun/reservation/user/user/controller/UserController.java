package com.jaehyun.reservation.user.user.controller;

import com.jaehyun.reservation.global.common.APIResponse;
import com.jaehyun.reservation.user.user.service.UserService;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
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

  @DeleteMapping("/quit")
  public APIResponse<Void> quit( Principal principal) {
    log.info("user quit 실행");
    return userService.quit(principal);
  }

  @PostMapping("/changeRoleAdmin")
  public APIResponse<String> changeRole(Principal principal) {
    return userService.changeRole(principal);
  }
}
