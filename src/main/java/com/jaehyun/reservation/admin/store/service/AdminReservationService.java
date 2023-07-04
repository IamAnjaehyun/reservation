package com.jaehyun.reservation.admin.store.service;

import com.jaehyun.reservation.admin.store.domain.entity.Store;
import com.jaehyun.reservation.admin.store.domain.repository.StoreRepository;
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
    List<ReservationResDto> reservationResDtoList = new ArrayList<>();
    List<Store> storeList = storeRepository.findAllByUser(user);

    for (Store store : storeList) {
      List<Reservation> reservationList = reservationRepository.findAllByStore(store);
      for (Reservation reservation : reservationList) {
        reservationResDtoList.add(mapToReservationResDto(reservation));
      }
    }
    return reservationResDtoList;
  }

  public List<ReservationResDto> getAllReservationList(Long storeId, Principal principal) {
    User user = userRepository.findByLoginId(principal.getName())
        .orElseThrow(NotExistUserException::new);
    Store store = storeRepository.findByUserAndId(user, storeId)
        .orElseThrow(NotExistStoreException::new);
    List<Reservation> reservationList = reservationRepository.findAllByStore(store);

    return mapToReservationResDtoList(reservationList);
  }

  public List<ReservationResDto> getStoreReservationListByStatus(Long storeId,
      ReservationStatus status, Principal principal) {
    User user = userRepository.findByLoginId(principal.getName())
        .orElseThrow(NotExistUserException::new);
    Store store = storeRepository.findByUserAndId(user, storeId)
        .orElseThrow(NotExistStoreException::new);
    List<Reservation> reservationList = reservationRepository.findAllByStoreAndStatus(store,
        status);

    return mapToReservationResDtoList(reservationList);
  }

  public List<ReservationResDto> getStoreReservationListByDateAndStatus(Long storeId,
      LocalDate localDate, ReservationStatus status, Principal principal) {
    User user = userRepository.findByLoginId(principal.getName())
        .orElseThrow(NotExistUserException::new);
    Store store = storeRepository.findByUserAndId(user, storeId)
        .orElseThrow(NotExistStoreException::new);
    LocalDateTime startDate = localDate.atStartOfDay();
    LocalDateTime endDate = localDate.atTime(LocalTime.MAX);
    List<Reservation> reservationList = reservationRepository
        .findAllByStoreAndStatusAndReservationDateTimeBetween(store, status, startDate, endDate);

    return mapToReservationResDtoList(reservationList);
  }

  public ReservationStatus changeReservationStatus(Long storeId,
      Long reservationId, ReservationStatus reservationStatus, ReservationStatus changeStatus,
      Principal principal) {
    //내가 가게의 사장인지 확인
    User user = userRepository.findByLoginId(principal.getName()).get();
    Store store = storeRepository.findByUserIdAndId(user.getId(), storeId)
        .orElseThrow(NotExistStoreException::new);
    Reservation reservation = reservationRepository.findByStoreIdAndIdAndStatus(store.getId(),
        reservationId, reservationStatus).orElseThrow(NotExistReservationException::new);
    reservation.setStatus(changeStatus);
    reservationRepository.save(reservation);

    return reservation.getStatus();
  }

  //단일 dto 생성
  private ReservationResDto mapToReservationResDto(Reservation reservation) {
    return ReservationResDto.builder()
        .storeId(reservation.getStore().getId())
        .userId(reservation.getUser().getId())
        .reservationId(reservation.getId())
        .reservationStatus(reservation.getStatus())
        .reservationPeopleNum(reservation.getReservationPeopleNum())
        .reservationDateTime(reservation.getReservationDateTime())
        .storeName(reservation.getStore().getName())
        .userName(reservation.getUser().getName())
        .build();
  }

  //return 용 list 생성
  private List<ReservationResDto> mapToReservationResDtoList(List<Reservation> reservationList) {
    List<ReservationResDto> reservationResDtoList = new ArrayList<>();
    for (Reservation reservation : reservationList) {
      reservationResDtoList.add(mapToReservationResDto(reservation));
    }
    return reservationResDtoList;
  }
}
