package com.jaehyun.reservation.global.exception.impl.review;

import com.jaehyun.reservation.global.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotExistReviewException extends AbstractException {
    static final String NOT_EXIST_REVIEW = "존재하지 않거나 접근할 수 없는 리뷰입니다.";

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return NOT_EXIST_REVIEW;
    }
}
