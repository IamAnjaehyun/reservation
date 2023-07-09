package com.jaehyun.reservation.admin.smsservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jaehyun.reservation.admin.smsservice.dto.SmsDto;
import com.jaehyun.reservation.admin.smsservice.dto.SmsResDto;
import com.jaehyun.reservation.admin.smsservice.service.SmsService;
import com.jaehyun.reservation.global.common.APIResponse;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reservation/admin/sms")
public class SmsController {
  private final SmsService smsService;


  @PostMapping
  public APIResponse<SmsResDto> sendSms(@RequestBody SmsDto messageDto, Principal principal)
      throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
    return APIResponse.success("sms", smsService.sendSms(messageDto));
  }
}