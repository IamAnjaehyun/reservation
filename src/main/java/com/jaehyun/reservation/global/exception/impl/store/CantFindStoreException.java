package com.jaehyun.reservation.global.exception.impl.store;

import com.jaehyun.reservation.global.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class CantFindStoreException extends AbstractException {
  static final String CANT_FIND_STORE = "상점 정보를 찾을 수 없습니다.";

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return CANT_FIND_STORE;
  }
}