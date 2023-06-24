package com.jaehyun.reservation.global.common;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class APIResponseHeader<T> {

  private int code;
  private String message;
}