package com.jaehyun.reservation.global.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse { //얘는 모델
    private int code;
    private String message;
}