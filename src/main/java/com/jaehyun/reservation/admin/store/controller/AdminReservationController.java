package com.jaehyun.reservation.admin.store.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.jaehyun.reservation.admin.store.service.AdminReservationService;
import com.jaehyun.reservation.global.common.APIResponse;
import com.jaehyun.reservation.user.reservation.domain.dto.ReservationResDto;
import com.jaehyun.reservation.user.type.ReservationStatus;
import io.swagger.annotations.Api;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = {"ADMIN RESERVATION API"}, description = "관리자 예약 관리 API")
@RequestMapping("/v1/reservation/admin/reservation")
public class AdminReservationController {

  private final AdminReservationService adminReservationService;


  //내 상점에 대한 예약 목록 확인
  @GetMapping
  public APIResponse<Page<ReservationResDto>> getAllReservationList(Principal principal, Pageable pageable) {
    return APIResponse.success("reservationList",
        adminReservationService.getAllStoreReservationList(principal,pageable));
  }

  //내 상점 클릭해서 특정 상점 예약 목록 전체 확인 / 상태 안넣으면 ALL /CANCEL/REFUSE/REQUEST/OKAY
  @GetMapping("/{storeId}")
  public APIResponse<Page<ReservationResDto>> getStoreReservationList(@PathVariable Long storeId,
      @RequestParam(required = false) ReservationStatus status, Principal principal, Pageable pageable) {
    return APIResponse.success("reservationList",
        adminReservationService.getStoreReservationListByStatus(storeId, status, principal, pageable));
  }

  //내 상점 클릭해서 특정 상점 특정 날짜 예약 목록 전체 요청 목록 확인 / 상태 안넣으면 ALL /CANCEL/REFUSE/REQUEST/OKAY
  @GetMapping("/date/{storeId}")
  public APIResponse<Page<ReservationResDto>> getReservationStatusList(@PathVariable Long storeId,
      @RequestParam(value = "localDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate localDate,
      @RequestParam(required = false) ReservationStatus status,
      Principal principal, Pageable pageable) {
    return APIResponse.success("reservationList",
        adminReservationService.getStoreReservationListByDateAndStatus(storeId, localDate,
            status, principal, pageable));
  }

  //예약 상태 OKAY로 변경
  @PostMapping("/change/{storeId}/{reservationId}/{reservationStatus}")
  public APIResponse<ReservationStatus> changeReservationStatus(@PathVariable Long storeId,
      @PathVariable Long reservationId,
      @PathVariable ReservationStatus reservationStatus,
      @RequestParam ReservationStatus changeStatus,
      Principal principal)
      throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {

    return APIResponse.success("statusChanged",
        adminReservationService.changeReservationStatus(
            storeId, reservationId, reservationStatus, changeStatus, principal));
  }
}
