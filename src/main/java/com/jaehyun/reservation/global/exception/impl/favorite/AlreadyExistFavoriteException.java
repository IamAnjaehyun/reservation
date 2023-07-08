package com.jaehyun.reservation.global.exception.impl.favorite;

import com.jaehyun.reservation.global.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyExistFavoriteException extends AbstractException {

  static final String ALREADY_EXIST_FAVORITE = "즐겨찾기 목록에 이미 존재하는 상점입니다.";

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return ALREADY_EXIST_FAVORITE;
  }
}

