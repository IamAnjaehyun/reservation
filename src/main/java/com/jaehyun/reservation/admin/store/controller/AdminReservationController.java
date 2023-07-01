package com.jaehyun.reservation.admin.store.controller;


import com.jaehyun.reservation.admin.store.service.AdminReservationService;
import com.jaehyun.reservation.global.common.APIResponse;
import com.jaehyun.reservation.user.reservation.domain.dto.ReservationResDto;
import com.jaehyun.reservation.user.reservation.domain.repository.ReservationRepository;
import com.jaehyun.reservation.user.type.ReservationStatus;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/v1/reservation/admin/reservation")
public class AdminReservationController {

  private final ReservationRepository reservationRepository;
  private final AdminReservationService adminReservationService;
  private static final String API_NAME = "reservationList";


  //내 상점 목록 확인
  @GetMapping
  public APIResponse<List<ReservationResDto>> getAllReservationList(Principal principal) {
    return APIResponse.success(API_NAME,
        adminReservationService.getAllStoreReservationList(principal));
  }

  //내 특정 상점 목록 확인
  @GetMapping("/{storeId}")
  public APIResponse<List<ReservationResDto>> getAllReservationByStoreList(
      @PathVariable Long storeId, Principal principal) {
    return APIResponse.success(
        API_NAME, adminReservationService.getAllReservationList(storeId, principal));
  }

  //내 상점 클릭해서 특정 상점 예약 목록 전체 확인 ALL/CANCEL/REFUSE/REQUEST/OKAY
  @GetMapping("/status/{storeId}")
  public APIResponse<List<ReservationResDto>> getStoreReservationList(@PathVariable Long storeId,
      @RequestParam ReservationStatus status, Principal principal) {
    return APIResponse.success(API_NAME,
        adminReservationService.getStoreReservationListByStatus(storeId, status, principal));
  }

  //내 상점 클릭해서 특정 상점 특정 날짜 예약 목록 전체 요청 목록 확인 ALL/CANCEL/REFUSE/REQUEST/OKAY
  @GetMapping("/status/date/{storeId}")
  public APIResponse<List<ReservationResDto>> getReservationStatusList(@PathVariable Long storeId,
      @RequestParam("localDateTime") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate localDateTime,
      @RequestParam ReservationStatus status,
      Principal principal) {
    return APIResponse.success(API_NAME,
        adminReservationService.getStoreReservationListByDateAndStatus(storeId, localDateTime,
            status, principal));
  }

  //예약 상태 OKAY로 변경
  @PostMapping("/change/{storeId}/{reservationId}/{reservationStatus}")
  public APIResponse<String> changeReservationStatus(@PathVariable Long storeId,
      @PathVariable Long reservationId,
      @PathVariable ReservationStatus reservationStatus,
      @RequestParam ReservationStatus changeStatus,
      Principal principal) {

    return APIResponse.success("status",
        adminReservationService.changeReservationStatus(
            storeId, reservationId, reservationStatus, changeStatus, principal));
  }
}
