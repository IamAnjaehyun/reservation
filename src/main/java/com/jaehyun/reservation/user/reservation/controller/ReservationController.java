package com.jaehyun.reservation.user.reservation.controller;

import com.jaehyun.reservation.global.common.APIResponse;
import com.jaehyun.reservation.user.reservation.domain.dto.ReservationReqDto;
import com.jaehyun.reservation.user.reservation.domain.dto.ReservationResDto;
import com.jaehyun.reservation.user.reservation.service.ReservationService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reservation/user/reservation")
public class ReservationController {

  private final ReservationService reservationService;

  @PostMapping("/{storeName}")
  public APIResponse<ReservationResDto> createReservation(@PathVariable String storeName,
      @RequestBody ReservationReqDto reservationReqDto, Principal principal) {
    return reservationService.createReservation(storeName, reservationReqDto, principal);
  }

  @GetMapping
  public APIResponse<List<ReservationResDto>> getReservationList(Principal principal) {
    return reservationService.getReservationList(principal);
  }

  @GetMapping("/{reservationId}")
  public APIResponse<ReservationResDto> getReservationDetail(@PathVariable Long reservationId,
      Principal principal) {
    return reservationService.getReservationDetail(reservationId, principal);
  }

  @PostMapping("/cancel/{reservationId}")
  public APIResponse<String> cancelReservation(@PathVariable Long reservationId,
      Principal principal) {
    return reservationService.cancelReservation(reservationId, principal);
  }
}
