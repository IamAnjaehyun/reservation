package com.jaehyun.reservation.user.reservation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jaehyun.reservation.admin.smsservice.controller.SmsController;
import com.jaehyun.reservation.admin.smsservice.dto.SmsDto;
import com.jaehyun.reservation.admin.smsservice.service.SmsService;
import com.jaehyun.reservation.admin.store.domain.entity.Store;
import com.jaehyun.reservation.admin.store.domain.repository.StoreRepository;
import com.jaehyun.reservation.global.exception.impl.reservation.DuplicateReservationException;
import com.jaehyun.reservation.global.exception.impl.reservation.NotExistReservationException;
import com.jaehyun.reservation.global.exception.impl.reservation.ReservationDateException;
import com.jaehyun.reservation.global.exception.impl.store.NotExistStoreException;
import com.jaehyun.reservation.global.exception.impl.user.NotExistUserException;
import com.jaehyun.reservation.user.reservation.domain.dto.ReservationReqDto;
import com.jaehyun.reservation.user.reservation.domain.dto.ReservationResDto;
import com.jaehyun.reservation.user.reservation.domain.entity.Reservation;
import com.jaehyun.reservation.user.reservation.domain.repository.ReservationRepository;
import com.jaehyun.reservation.user.type.ReservationStatus;
import com.jaehyun.reservation.user.user.domain.entity.User;
import com.jaehyun.reservation.user.user.domain.repository.UserRepository;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

  private final StoreRepository storeRepository;
  private final UserRepository userRepository;
  private final ReservationRepository reservationRepository;
  private final SmsService smsService;


  public ReservationResDto createReservation(Long storeId,
      ReservationReqDto reservationReqDto, Principal principal)
      throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
    Store store = storeRepository.findById(storeId)
        .orElseThrow(NotExistStoreException::new);
    User user = userRepository.findByLoginId(principal.getName())
        .orElseThrow(NotExistUserException::new);

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
    Optional<User> user = Optional.ofNullable(userRepository.findByLoginId(principal.getName())
        .orElseThrow(NotExistUserException::new));

    List<ReservationResDto> reservationResDtoList = new ArrayList<>();
    List<Reservation> reservationList = reservationRepository.findAllByUser(user.get());

    for (Reservation reservation : reservationList) {
      reservationResDtoList.add(ReservationResDto.fromReservation(reservation));
    }
    return reservationResDtoList;
  }

  public ReservationResDto getReservationDetail(Long reservationId,
      Principal principal) {
    Optional<User> user = Optional.ofNullable(userRepository.findByLoginId(principal.getName())
        .orElseThrow(NotExistUserException::new));

    Optional<Reservation> reservationOptional = Optional.ofNullable(
        reservationRepository.findByUserAndId(user.get(), reservationId)
            .orElseThrow(NotExistStoreException::new));

    Reservation reservation = reservationOptional.get();
    return ReservationResDto.fromReservation(reservation);
  }

  public ReservationStatus cancelReservation(Long reservationId, Principal principal)
      throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
    User user = userRepository.findByLoginId(principal.getName())
        .orElseThrow(NotExistUserException::new);

    Optional<Reservation> reservationOptional = Optional.ofNullable(
        reservationRepository.findByUserAndId(user, reservationId)
            .orElseThrow(NotExistStoreException::new));

    Reservation reservation = reservationOptional.get();
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

  public void checkAndSendSms()
      throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
    List<Reservation> reservationList = reservationRepository.findAllByStatus(
        ReservationStatus.OKAY);
    for (Reservation reservation : reservationList) {
      LocalDateTime reservationDateTimeMinus30Minutes = reservation.getReservationDateTime()
          .minusMinutes(30);
      LocalDateTime currentDateTime = LocalDateTime.now();

      if (reservationDateTimeMinus30Minutes.isBefore(currentDateTime)) {
        User user = reservation.getUser(); // User 객체를 가져오는 방법이 구현되어 있다고 가정

        SmsDto smsDto = new SmsDto().builder()
            .to(user.getPhoneNum())
            .content(reservation.getStore().getName() + "\n" + user.getName() + "님께서 "
                + reservation.getReservationDateTime()
                + "에 예약하신 예약시간까지 30분 남았습니다. 현 시간 이후로 예약 취소는 불가능합니다.")
            .build();
        smsService.sendSms(smsDto);

        //문자를 보냈다면 더이상 예약 취소가 불가능 하기 때문에 상태를 USED로 변경
        reservation.setStatus(ReservationStatus.USED);
        reservationRepository.save(reservation);
      }
    }
  }
}
