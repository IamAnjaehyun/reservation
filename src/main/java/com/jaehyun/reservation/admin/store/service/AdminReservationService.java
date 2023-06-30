package com.jaehyun.reservation.admin.store.service;

import com.jaehyun.reservation.admin.store.domain.entity.Store;
import com.jaehyun.reservation.admin.store.domain.repository.StoreRepository;
import com.jaehyun.reservation.global.common.APIResponse;
import com.jaehyun.reservation.global.exception.impl.reservation.NotExistReservationException;
import com.jaehyun.reservation.global.exception.impl.store.NotExistStoreException;
import com.jaehyun.reservation.global.exception.impl.user.NotExistUserException;
import com.jaehyun.reservation.user.reservation.domain.dto.ReservationResDto;
import com.jaehyun.reservation.user.reservation.domain.entity.Reservation;
import com.jaehyun.reservation.user.reservation.domain.repository.ReservationRepository;
import com.jaehyun.reservation.user.type.ReservationStatus;
import com.jaehyun.reservation.user.user.domain.entity.User;
import com.jaehyun.reservation.user.user.domain.repository.UserRepository;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminReservationService {

  private final ReservationRepository reservationRepository;
  private final StoreRepository storeRepository;
  private final UserRepository userRepository;


  public List<ReservationResDto> getAllStoreReservationList(Principal principal) {
    User user = userRepository.findByLoginId(principal.getName())
        .orElseThrow(NotExistUserException::new);

    List<Store> storeList = storeRepository.findAllByUser(user); //사장이 갖고있는 모든 상점들 list

    List<ReservationResDto> reservationResDtoList = new ArrayList<>();

    for (Store store : storeList) { //갖고있는 모든 상점
      List<Reservation> reservationList = reservationRepository.findAllByStore(
          store); //상점에게 요청된 모든 예약들
      for (Reservation reservation : reservationList) {
        ReservationResDto reservationResDto = ReservationResDto.builder()
            .storeId(reservation.getStore().getId())
            .userId(reservation.getUser().getId())
            .reservationId(reservation.getId())
            .reservationStatus(reservation.getStatus())
            .reservationPeopleNum(reservation.getReservationPeopleNum())
            .reservationDateTime(reservation.getReservationDateTime())
            .storeName(reservation.getStore().getName())
            .userName(reservation.getUser().getName())
            .build();
        reservationResDtoList.add(reservationResDto);
      }
    }

    return reservationResDtoList;
  }

  public List<ReservationResDto> getAllReservationList(Long storeId, Principal principal) {
    User user = userRepository.findByLoginId(principal.getName())
        .orElseThrow(NotExistUserException::new);

    Store store = storeRepository.findByUserAndId(user, storeId)
        .orElseThrow(NotExistStoreException::new);

    List<ReservationResDto> reservationResDtoList = new ArrayList<>();

    List<Reservation> reservationList = reservationRepository.findAllByStore(store); //예약 목록
    for (Reservation reservation : reservationList) {
      if (reservation.getStore().getId().equals(storeId)) {
        ReservationResDto reservationResDto = ReservationResDto.builder()
            .storeId(reservation.getStore().getId())
            .userId(reservation.getUser().getId())
            .reservationId(reservation.getId())
            .reservationStatus(reservation.getStatus())
            .reservationPeopleNum(reservation.getReservationPeopleNum())
            .reservationDateTime(reservation.getReservationDateTime())
            .storeName(reservation.getStore().getName())
            .userName(reservation.getUser().getName())
            .build();
        reservationResDtoList.add(reservationResDto);
      }
    }
    return reservationResDtoList;
  }

  public List<ReservationResDto> getStoreReservationListByStatus(Long storeId,
      ReservationStatus status, Principal principal) {
    User user = userRepository.findByLoginId(principal.getName())
        .orElseThrow(NotExistUserException::new);

    Store store = storeRepository.findByUserAndId(user, storeId)
        .orElseThrow(NotExistStoreException::new);

    List<ReservationResDto> reservationResDtoList = new ArrayList<>();

    List<Reservation> reservationList = reservationRepository.findAllByStoreAndStatus(store,
        status); //예약 목록
    for (Reservation reservation : reservationList) {
      if (reservation.getStore().getId().equals(storeId)) {
        ReservationResDto reservationResDto = ReservationResDto.builder()
            .storeId(reservation.getStore().getId())
            .userId(reservation.getUser().getId())
            .reservationId(reservation.getId())
            .reservationStatus(reservation.getStatus())
            .reservationPeopleNum(reservation.getReservationPeopleNum())
            .reservationDateTime(reservation.getReservationDateTime())
            .storeName(reservation.getStore().getName())
            .userName(reservation.getUser().getName())
            .build();
        reservationResDtoList.add(reservationResDto);
      }
    }
    return reservationResDtoList;
  }

  public List<ReservationResDto> getStoreReservationListByDateAndStatus(Long storeId,
      LocalDate localDateTime, ReservationStatus status, Principal principal) {
    User user = userRepository.findByLoginId(principal.getName())
        .orElseThrow(NotExistUserException::new);

    Store store = storeRepository.findByUserAndId(user, storeId)
        .orElseThrow(NotExistStoreException::new);

    List<ReservationResDto> reservationResDtoList = new ArrayList<>();

    LocalDateTime startDate = localDateTime.atStartOfDay();
    LocalDateTime endDate = localDateTime.atTime(LocalTime.MAX);

    List<Reservation> reservationList = reservationRepository.findAllByStoreAndStatusAndReservationDateTimeBetween(
        store, status, startDate, endDate); // 예약 목록

    for (Reservation reservation : reservationList) {
      if (reservation.getStore().getId().equals(storeId)) {
        ReservationResDto reservationResDto = ReservationResDto.builder()
            .storeId(reservation.getStore().getId())
            .userId(reservation.getUser().getId())
            .reservationId(reservation.getId())
            .reservationStatus(reservation.getStatus())
            .reservationPeopleNum(reservation.getReservationPeopleNum())
            .reservationDateTime(reservation.getReservationDateTime())
            .storeName(reservation.getStore().getName())
            .userName(reservation.getUser().getName())
            .build();
        reservationResDtoList.add(reservationResDto);
      }
    }
    return reservationResDtoList;
  }

  public String changeReservationStatus(Long storeId,
      Long reservationId, ReservationStatus reservationStatus, ReservationStatus changeStatus,
      Principal principal) {
    Optional<User> userOptional = Optional.ofNullable(
        userRepository.findByLoginId(principal.getName()).orElseThrow(NotExistUserException::new));

    Optional<Store> storeOptional = Optional.ofNullable(
        storeRepository.findByIdAndUserId(storeId, userOptional.get().getId())
            .orElseThrow(NotExistStoreException::new));

    Optional<Reservation> reservationOptional = Optional.ofNullable(
        reservationRepository.findByStoreIdAndIdAndStatus(
                storeOptional.get().getId(), reservationId, reservationStatus)
            .orElseThrow(NotExistReservationException::new));

    Reservation reservation = reservationOptional.get();
    reservation.setStatus(changeStatus);
    reservationRepository.save(reservation);

    return reservation.getStatus().toString();
  }
}
