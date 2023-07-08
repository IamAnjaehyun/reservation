package com.jaehyun.reservation.admin.store.smsservice.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SmsResDto {
  private String requestId;
  private LocalDateTime requestTime;
  private String statusCode;
  private String statusName;
}
