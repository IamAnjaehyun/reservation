package com.jaehyun.reservation.user.reservation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jaehyun.reservation.admin.smsservice.dto.SmsDto;
import com.jaehyun.reservation.admin.smsservice.service.SmsService;
import com.jaehyun.reservation.admin.store.domain.entity.Store;
import com.jaehyun.reservation.admin.store.domain.repository.StoreRepository;
import com.jaehyun.reservation.global.exception.impl.reservation.DuplicateReservationException;
import com.jaehyun.reservation.global.exception.impl.reservation.ReservationDateException;
import com.jaehyun.reservation.global.exception.impl.store.NotExistStoreException;
import com.jaehyun.reservation.user.reservation.domain.dto.ReservationReqDto;
import com.jaehyun.reservation.user.reservation.domain.dto.ReservationResDto;
import com.jaehyun.reservation.user.reservation.domain.entity.Reservation;
import com.jaehyun.reservation.user.reservation.domain.repository.ReservationRepository;
import com.jaehyun.reservation.user.type.ReservationStatus;
import com.jaehyun.reservation.user.user.domain.entity.User;
import com.jaehyun.reservation.user.user.service.UserService;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

  private final StoreRepository storeRepository;
  private final UserService userService;
  private final ReservationRepository reservationRepository;
  private final SmsService smsService;


  public ReservationResDto createReservation(Long storeId,
      ReservationReqDto reservationReqDto, Principal principal)
      throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
    Store store = storeRepository.findById(storeId).orElseThrow(NotExistStoreException::new);
    User user = userService.findUserByPrincipal(principal);

    // 예약 날짜가 현재 시간보다 이전인 경우 예외 처리
    if (reservationReqDto.getReservationDateTime().isBefore(LocalDateTime.now())) {
      throw new ReservationDateException();
    }

    //같은 시간에 이미 예약한경우 예외처리
    if (reservationRepository.existsByReservationDateTimeAndUser(
        reservationReqDto.getReservationDateTime(), user)) {
      throw new DuplicateReservationException();
    }

    // 예약 생성
    Reservation reservation = Reservation.builder()
        .user(user)
        .store(store)
        .reservationDateTime(reservationReqDto.getReservationDateTime())
        .reservationPeopleNum(reservationReqDto.getReservationPeopleNum())
        .status(ReservationStatus.REQUEST) // 예약 요청 상태로 설정
        .build();
    reservationRepository.save(reservation);

    //예약 신청시 문자 발송
    SmsDto smsDto = new SmsDto().builder()
        .to(user.getPhoneNum())
        .content(reservation.getStore().getName() + "\n" + user.getName() + "님께서 "
            + reservation.getReservationDateTime() + "에 요청하신 예약이 신청 완료되었습니다. ")
        .build();
//    예약 신청이 오면 상점 관리자 번호로 예약 내역 전송
//    SmsDto smsDtoToManager = new SmsDto().builder()
//        .to(store.getPhoneNum())
//        .content(reservation.getStore().getName() + "\n" + user.getName() + "님께서 "
//            + reservation.getReservationDateTime() + "에 "
//            + reservation.getReservationPeopleNum()+ " 인 방문 예약을 요청하였습니다. ")
//        .build();
//    smsService.sendSms(smsDtoToManager);
    smsService.sendSms(smsDto);

    return ReservationResDto.fromReservation(reservation);
  }

  public List<ReservationResDto> getReservationList(Principal principal) {
    User user = userService.findUserByPrincipal(principal);

    List<ReservationResDto> reservationResDtoList = new ArrayList<>();
    List<Reservation> reservationList = reservationRepository.findAllByUser(user);

    for (Reservation reservation : reservationList) {
      reservationResDtoList.add(ReservationResDto.fromReservation(reservation));
    }
    return reservationResDtoList;
  }

  public ReservationResDto getReservationDetail(Long reservationId,
      Principal principal) {
    User user = userService.findUserByPrincipal(principal);

    Reservation reservation = reservationRepository.findByUserAndId(user, reservationId)
        .orElseThrow(NotExistStoreException::new);

    return ReservationResDto.fromReservation(reservation);
  }

  public ReservationStatus cancelReservation(Long reservationId, Principal principal)
      throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
    User user = userService.findUserByPrincipal(principal);

    Reservation reservation = reservationRepository.findByUserAndId(user, reservationId)
        .orElseThrow(NotExistStoreException::new);

    reservation.setStatus(ReservationStatus.CANCEL);
    reservationRepository.save(reservation);

    //예약 취소시 문자 발송
    SmsDto smsDto = new SmsDto().builder()
        .to(user.getPhoneNum())
        .content(reservation.getStore().getName() + "\n" + user.getName() + "님께서 "
            + reservation.getReservationDateTime() + "에 요청하신 예약이 취소 완료되었습니다. ")
        .build();
//    예약 취소시 상점 관리자 번호로 예약 취소 내역 전송
//    Store store = reservationRepository.findById(reservationId).get().getStore();
//    SmsDto smsDtoToManager = new SmsDto().builder()
//        .to(store.getPhoneNum())
//        .content(reservation.getStore().getName() + "\n" + user.getName() + "님께서 "
//            + reservation.getReservationDateTime() + "에 요청하신 예약이 취소 처리 되었습니다. ")
//        .build();
//    smsService.sendSms(smsDtoToManager);
    smsService.sendSms(smsDto);
    return reservation.getStatus();
  }
}
