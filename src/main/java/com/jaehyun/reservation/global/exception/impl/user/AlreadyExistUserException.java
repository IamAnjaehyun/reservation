package com.jaehyun.reservation.global.exception.impl.user;

import com.jaehyun.reservation.global.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyExistUserException extends AbstractException {
    static final String ALREADY_EXIST_LOGIN_ID = "이미 존재하는 사용자 아이디입니다.";

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return ALREADY_EXIST_LOGIN_ID;
    }
}