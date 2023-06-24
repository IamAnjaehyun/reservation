package com.jaehyun.reservation.global.exception.impl.reservation;

import com.jaehyun.reservation.global.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotExistReservationException extends AbstractException {

  static final String NOT_EXIST_RESERVATION = "존재하지 않는 예약입니다.";

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return NOT_EXIST_RESERVATION;
  }
}
