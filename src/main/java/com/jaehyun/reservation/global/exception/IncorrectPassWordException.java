package com.jaehyun.reservation.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class IncorrectPassWordException extends RuntimeException {

  static final String INCORRECT_PASSWORD = "비밀번호가 잘못되었습니다.";

  public IncorrectPassWordException() {
    super(INCORRECT_PASSWORD);
  }
}
