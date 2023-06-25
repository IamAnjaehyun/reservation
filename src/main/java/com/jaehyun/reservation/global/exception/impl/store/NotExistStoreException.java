package com.jaehyun.reservation.global.exception.impl.store;

import com.jaehyun.reservation.global.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotExistStoreException extends AbstractException {
  static final String NOT_EXIST_STORE = "존재하지 않는 상점입니다.";

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return NOT_EXIST_STORE;
  }
}