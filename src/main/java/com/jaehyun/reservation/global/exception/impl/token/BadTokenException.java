package com.jaehyun.reservation.global.exception.impl.token;

import com.jaehyun.reservation.global.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class BadTokenException extends AbstractException {

  static final String BAD_TOKEN = "옳지 않은 토큰을 통한 요청입니다.";

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return BAD_TOKEN;
  }
}
