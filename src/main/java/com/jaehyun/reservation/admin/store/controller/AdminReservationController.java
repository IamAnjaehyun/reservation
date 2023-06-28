package com.jaehyun.reservation.admin.store.controller;


import com.jaehyun.reservation.admin.store.service.AdminReservationService;
import com.jaehyun.reservation.global.common.APIResponse;
import com.jaehyun.reservation.user.reservation.domain.dto.ReservationResDto;
import com.jaehyun.reservation.user.reservation.domain.repository.ReservationRepository;
import com.jaehyun.reservation.user.type.ReservationStatus;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  //내 상점 목록 확인
  @GetMapping
  public APIResponse<List<ReservationResDto>> getAllReservationList(Principal principal) {
    return adminReservationService.getAllStoreReservationList(principal);
  }

  //내 특정 상점 목록 확인
  @GetMapping("/{storeId}")
  public APIResponse<List<ReservationResDto>> getAllReservationByStoreList(
      @PathVariable String storeId, Principal principal) {
    return adminReservationService.getAllReservationList(principal);
  }

  //내 상점 클릭해서 특정 상점 예약 목록 전체 확인 ALL/CANCEL/REFUSE/REQUEST/OKAY
  @GetMapping("/{storeId}/{status}")
  public APIResponse<List<ReservationResDto>> getStoreReservationList(@PathVariable String storeId,
      @PathVariable ReservationStatus status, Principal principal) {
    return adminReservationService.getStoreReservationListByStatus(principal);
  }

  //내 상점 클릭해서 특정 상점 특정 날짜 예약 목록 전체 요청 목록 확인 ALL/CANCEL/REFUSE/REQUEST/OKAY
  @GetMapping("/{storeId}/{date}/{status}")
  public APIResponse<List<ReservationResDto>> getReservationStatusList(@PathVariable String storeId,
      @PathVariable LocalDateTime localDateTime,
      @PathVariable ReservationStatus status,
      Principal principal) {
    return adminReservationService.getStoreReservationListByDateAndStatus(principal);
  }

  //예약 상태 OKAY로 변경
  @PostMapping("/{storeId}/{reservationId}/{reservationStatus}/change")
  public APIResponse<String> changeReservationStatus(@PathVariable Long storeId,
      @PathVariable Long reservationId,
      @PathVariable ReservationStatus reservationStatus,
      @RequestParam ReservationStatus changeStatus,
      Principal principal) {
    return adminReservationService.changeReservationStatus(storeId, reservationId,
        reservationStatus, changeStatus,principal);
  }
}
