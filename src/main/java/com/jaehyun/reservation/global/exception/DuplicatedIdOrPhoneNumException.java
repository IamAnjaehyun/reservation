package com.jaehyun.reservation.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicatedIdOrPhoneNumException extends RuntimeException {

  static final String DUPLICATED_ID_PHONE_NUM = "중복되는 아이디 혹은 휴대폰 번호가 존재합니다.";

  public DuplicatedIdOrPhoneNumException() {
    super(DUPLICATED_ID_PHONE_NUM);
  }
}
