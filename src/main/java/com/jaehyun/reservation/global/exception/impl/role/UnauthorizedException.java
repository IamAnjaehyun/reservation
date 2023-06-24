package com.jaehyun.reservation.global.exception.impl.role;

import com.jaehyun.reservation.global.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends AbstractException {
    static final String UNAUTHORIZED = "옳지 않은 권한을 통한 접근입니다.";

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return UNAUTHORIZED;
    }
}
