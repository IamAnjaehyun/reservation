package com.jaehyun.reservation.global.exception.impl.reservation;

import com.jaehyun.reservation.global.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class ReservationDateException extends AbstractException {

    static final String CHECK_DATE = "날짜 및 시간을 확인해 주세요.";
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return CHECK_DATE;
    }
}
