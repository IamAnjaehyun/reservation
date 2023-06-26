package com.jaehyun.reservation.global.exception.impl.store;

import com.jaehyun.reservation.global.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyExistStoreException extends AbstractException {
    static final String DUPLICATED_STORE_NAME = "같은 이름의 상점이 존재합니다.";

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return DUPLICATED_STORE_NAME;
    }
}