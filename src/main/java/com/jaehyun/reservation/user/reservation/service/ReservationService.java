package com.jaehyun.reservation.user.reservation.service;

import com.jaehyun.reservation.admin.store.domain.entity.Store;
import com.jaehyun.reservation.admin.store.domain.repository.StoreRepository;
import com.jaehyun.reservation.global.common.APIResponse;
import com.jaehyun.reservation.global.exception.impl.reservation.NotExistReservationException;
import com.jaehyun.reservation.global.exception.impl.reservation.ReservationDateException;
import com.jaehyun.reservation.global.exception.impl.store.NotExistStoreException;
import com.jaehyun.reservation.global.exception.impl.user.DuplicatedIdOrPhoneNumException;
import com.jaehyun.reservation.global.exception.impl.user.NotExistUserException;
import com.jaehyun.reservation.user.reservation.domain.dto.ReservationReqDto;
import com.jaehyun.reservation.user.reservation.domain.dto.ReservationResDto;
import com.jaehyun.reservation.user.reservation.domain.entity.Reservation;
import com.jaehyun.reservation.user.reservation.domain.repository.ReservationRepository;
import com.jaehyun.reservation.user.type.ReservationStatus;
import com.jaehyun.reservation.user.user.domain.entity.User;
import com.jaehyun.reservation.user.user.domain.repository.UserRepository;
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
  private final String API_NAME = "reservation";


  public APIResponse<ReservationResDto> createReservation(String storeName,
      ReservationReqDto reservationReqDto, Principal principal) {
    // 예약 날짜 비교 로직 구현 (원하는 비교 조건에 따라 수정 필요)

    if (reservationReqDto.getReservationDateTime().isBefore(LocalDateTime.now())) {
      // 예약 날짜가 현재 시간보다 이전인 경우 예외 처리
      throw new ReservationDateException();
    }

    Store store = storeRepository.findByName(storeName)
        .orElseThrow(NotExistStoreException::new);
    User user = userRepository.findByLoginId(principal.getName())
        .orElseThrow(NotExistUserException::new);

    // 예약 생성
    Reservation reservation = Reservation.builder()
        .user(user)
        .store(store)
        .reservationDateTime(reservationReqDto.getReservationDateTime())
        .reservationPeopleNum(reservationReqDto.getReservationPeopleNum())
        .status(ReservationStatus.REQUEST) // 예약 요청 상태로 설정
        .build();
    reservationRepository.save(reservation);

    ReservationResDto reservationResDto = ReservationResDto.builder()
        .storeName(reservation.getStore().getName())
        .userName(reservation.getUser().getName())
        .reservationDateTime(reservation.getReservationDateTime())
        .reservationPeopleNum(reservation.getReservationPeopleNum())
        .build();

    return APIResponse.success(API_NAME, reservationResDto);
  }

  public APIResponse<List<ReservationResDto>> getReservationList(Principal principal) {
    Optional<User> user = Optional.ofNullable(userRepository.findByLoginId(principal.getName())
        .orElseThrow(NotExistUserException::new));

    List<ReservationResDto> reservationResDtoList = new ArrayList<>();
    List<Reservation> reservationList = reservationRepository.findAllByUser(user.get());

    for (Reservation reservation : reservationList) {
      ReservationResDto reservationResDto = ReservationResDto.builder()
          .userName(reservation.getUser().getName())
          .storeName(reservation.getStore().getName())
          .reservationId(reservation.getId())
          .reservationDateTime(reservation.getReservationDateTime())
          .reservationPeopleNum(reservation.getReservationPeopleNum())
          .build();
      reservationResDtoList.add(reservationResDto);
    }
    return APIResponse.success(API_NAME, reservationResDtoList);
  }

  public APIResponse<ReservationResDto> getReservationDetail(Long reservationId,
      Principal principal) {
    Optional<User> user = Optional.ofNullable(userRepository.findByLoginId(principal.getName())
        .orElseThrow(NotExistUserException::new));

    Optional<Reservation> reservationOptional = Optional.ofNullable(
        reservationRepository.findByUserAndId(user.get(), reservationId)
            .orElseThrow(NotExistStoreException::new));

    Reservation reservation = reservationOptional.get();
    ReservationResDto reservationResDto = ReservationResDto.builder()
        .userName(reservation.getUser().getName())
        .storeName(reservation.getStore().getName())
        .reservationId(reservation.getId())
        .reservationDateTime(reservation.getReservationDateTime())
        .reservationPeopleNum(reservation.getReservationPeopleNum())
        .build();

    return APIResponse.success(API_NAME, reservationResDto);
  }
}
