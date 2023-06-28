package com.jaehyun.reservation.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class APIResponse<T> {
  private final static String SUCCESS_MESSAGE = "SUCCESS";

  private final APIResponseHeader header;
  private final T body;

  public static <T> APIResponse<T> success(String name, T body) {
    Map<String, T> map = new HashMap<>();
    map.put(name, body);
    return new APIResponse(new APIResponseHeader(HttpStatus.OK.value(), SUCCESS_MESSAGE), map);
  }

  public static <T> APIResponse<T> success(T body) {
    return new APIResponse(new APIResponseHeader(HttpStatus.OK.value(), SUCCESS_MESSAGE), body);
  }

  public static <T> APIResponse<T> create(String name, T body) {
    Map<String, T> map = new HashMap<>();
    map.put(name, body);
    return new APIResponse(new APIResponseHeader(HttpStatus.CREATED.value(), SUCCESS_MESSAGE), map);
  }

  public static <T> APIResponse<T> delete() {
    return new APIResponse(new APIResponseHeader(HttpStatus.OK.value(), SUCCESS_MESSAGE), null);
  }
}