//package com.jaehyun.reservation.user.reservation.controller;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.jaehyun.reservation.user.reservation.service.ReservationService;
//import java.io.UnsupportedEncodingException;
//import java.net.URISyntaxException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//

//리뷰 후 삭제예정
//@Slf4j
//@Component
//@RequiredArgsConstructor
//@EnableScheduling
//public class SmsScheduler {
//  private final ReservationService reservationService;
//
//  @Scheduled(cron = "0 * * * * *") // 매 분마다 실행
//  public void checkAndSendSms()
//      throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
//    reservationService.checkAndSendSms();
//  }
//}