package com.jaehyun.reservation.global.exception.impl.reservation;

import org.springframework.http.HttpStatus;

public class ReservationDateException extends AbstractMethodError{

    static final String CHECK_DATE = "날짜를 확인해 주세요.";
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return CHECK_DATE;
    }
}
