package com.jaehyun.reservation.global.common;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class APIResponseHeader<T> implements Serializable {

  private int code;
  private String message;
}