package com.jaehyun.reservation.global.exception.impl.user;

import com.jaehyun.reservation.global.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class PasswordNotMatchException extends AbstractException {
    static final String NOT_CORRECT_PASSWORD = "비밀번호가 일치하지 않습니다";

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return NOT_CORRECT_PASSWORD;
    }
}