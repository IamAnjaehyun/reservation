package com.jaehyun.reservation.user.type;

//사장 승인이 있어야 예약완료가능
public enum ReservationStatus {
  REFUSE,
  PLEASE_WAIT,
  OKAY;

  public static final ReservationStatus DEFAULT = PLEASE_WAIT;
}
