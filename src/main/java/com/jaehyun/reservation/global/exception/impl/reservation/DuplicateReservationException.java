package com.jaehyun.reservation.global.exception.impl.reservation;

import com.jaehyun.reservation.global.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class DuplicateReservationException extends AbstractException {

    static final String DUPLICATE_RESERVATION = "동일 시간에 대해 이미 진행중인 예약이 존재합니다.";
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return DUPLICATE_RESERVATION;
    }
}
