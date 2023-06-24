package com.jaehyun.reservation.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class APIResponse<T> {

  private final static int SUCCESS = 200;
  private final static int CREATE = 201;
  private final static int NO_CONTENT = 204;
  private final static int BAD_REQUEST = 400;
  private final static int NOT_FOUND = 404;
  private final static int CONFLICT = 409;
  private final static int FAILED = 500;

  private final static String SUCCESS_MESSAGE = "SUCCESS";

  private final APIResponseHeader header;
  private final T body;

  public static <T> APIResponse<T> success(String name, T body) {
    Map<String, T> map = new HashMap<>();
    map.put(name, body);
    return new APIResponse(new APIResponseHeader(SUCCESS, SUCCESS_MESSAGE), map);
  }

  public static <T> APIResponse<T> success(T body) {
    return new APIResponse(new APIResponseHeader(SUCCESS, SUCCESS_MESSAGE), body);
  }

  public static <T> APIResponse<T> create(String name, T body) {
    Map<String, T> map = new HashMap<>();
    map.put(name, body);
    return new APIResponse(new APIResponseHeader(CREATE, SUCCESS_MESSAGE), map);
  }

  public static <T> APIResponse<T> delete() {
    return new APIResponse(new APIResponseHeader(SUCCESS, SUCCESS_MESSAGE), null);
  }
}