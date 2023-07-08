package com.jaehyun.reservation.global.exception.impl.favorite;

import com.jaehyun.reservation.global.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotFavoriteStoreException extends AbstractException {

  static final String NOT_FAVORITE_STORE = "즐겨찾기 목록에 존재하지 않습니다.";

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return NOT_FAVORITE_STORE;
  }
}

