package com.jaehyun.reservation.global.exception.impl.user;

import com.jaehyun.reservation.global.exception.AbstractException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class DuplicatedIdOrPhoneNumException extends AbstractException {

  static final String DUPLICATED_ID_PHONE_NUM = "중복되는 아이디 혹은 휴대폰 번호가 존재합니다.";
  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return DUPLICATED_ID_PHONE_NUM;
  }
}
