package com.jaehyun.reservation.global.exception.impl.reservation;

import org.springframework.http.HttpStatus;

public class NotUseThisStoreException extends AbstractMethodError{

    static final String NOT_USED_STORE = "이용하지 않은 상점에 대해 리뷰를 작성할 수 없습니다.";
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return NOT_USED_STORE;
    }
}
