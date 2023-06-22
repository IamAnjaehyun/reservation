package com.jaehyun.reservation.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotExistUserException extends RuntimeException {

  static final String NOT_EXIST_USER = "존재하지 않는 회원입니다.";

  public NotExistUserException() {
    super(NOT_EXIST_USER);
  }
}
