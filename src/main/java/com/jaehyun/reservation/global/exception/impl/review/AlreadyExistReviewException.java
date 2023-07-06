package com.jaehyun.reservation.global.exception.impl.review;

import com.jaehyun.reservation.global.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyExistReviewException extends AbstractException {
    static final String ALREADY_EXIST_REVIEW = "이미 예약건에 대해 작성한 리뷰가 존재합니다.";

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return ALREADY_EXIST_REVIEW;
    }
}
