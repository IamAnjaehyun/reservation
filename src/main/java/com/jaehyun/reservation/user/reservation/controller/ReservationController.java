package com.jaehyun.reservation.user.reservation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jaehyun.reservation.global.common.APIResponse;
import com.jaehyun.reservation.user.reservation.domain.dto.ReservationReqDto;
import com.jaehyun.reservation.user.reservation.domain.dto.ReservationResDto;
import com.jaehyun.reservation.user.reservation.service.ReservationService;
import com.jaehyun.reservation.user.type.ReservationStatus;
import io.swagger.annotations.Api;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
@Api(tags = {"RESERVATION API"}, description = "고객 예약 API")
@RequestMapping("/v1/reservation/user/reservation")
public class ReservationController {

  private final ReservationService reservationService;

  @PostMapping("/{storeId}")
  public APIResponse<ReservationResDto> createReservation(@PathVariable Long storeId,
      @RequestBody ReservationReqDto reservationReqDto, Principal principal)
      throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
    return APIResponse.success("reservation",
        reservationService.createReservation(storeId, reservationReqDto, principal));
  }

  @GetMapping
  public APIResponse<List<ReservationResDto>> getReservationList(Principal principal) {
    return APIResponse.success("reservationList", reservationService.getReservationList(principal));
  }

  @GetMapping("/{reservationId}")
  public APIResponse<ReservationResDto> getReservationDetail(@PathVariable Long reservationId,
      Principal principal) {
    return APIResponse.success("reservation",
        reservationService.getReservationDetail(reservationId, principal));
  }

  @PostMapping("/cancel/{reservationId}")
  public APIResponse<ReservationStatus> cancelReservation(@PathVariable Long reservationId,
      Principal principal)
      throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
    return APIResponse.success("statusChanged",
        reservationService.cancelReservation(reservationId, principal));
  }
}
