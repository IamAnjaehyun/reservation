package com.jaehyun.reservation.global.exception.impl.store;

import com.jaehyun.reservation.global.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class InvalidStoreNameException extends AbstractException {
    static final String INVALID_STORE_NAME = "상점 이름이 옳지 않습니다.";
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return INVALID_STORE_NAME;
    }
}
