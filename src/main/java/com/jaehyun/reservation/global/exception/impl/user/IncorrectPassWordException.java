package com.jaehyun.reservation.global.exception.impl.user;

import com.jaehyun.reservation.global.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class IncorrectPassWordException extends AbstractException {

  static final String INCORRECT_PASSWORD = "비밀번호가 잘못되었습니다.";
  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return INCORRECT_PASSWORD;
  }
}
