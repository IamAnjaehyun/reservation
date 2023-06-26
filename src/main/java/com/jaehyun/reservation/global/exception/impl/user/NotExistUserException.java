package com.jaehyun.reservation.global.exception.impl.user;

import com.jaehyun.reservation.global.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotExistUserException extends AbstractException {
  static final String NOT_EXIST_USER = "존재하지 않는 회원입니다.";

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return NOT_EXIST_USER;
  }
}