package com.jaehyun.reservation.admin.store.smsservice.dto;

import java.util.List;
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
public class SmsReqDto {
  private String type;
  private String contentType;
  private String countryCode;
  private String from;
  private String content;
  private List<SmsDto> messages;
}
