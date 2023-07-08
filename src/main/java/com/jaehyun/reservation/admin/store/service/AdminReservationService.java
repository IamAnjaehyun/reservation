package com.jaehyun.reservation.admin.store.service;

import com.jaehyun.reservation.admin.store.domain.entity.Store;
import com.jaehyun.reservation.admin.store.domain.repository.StoreRepository;
import com.jaehyun.reservation.global.exception.impl.reservation.NotExistReservationException;
import com.jaehyun.reservation.global.exception.impl.store.CantFindStoreException;
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
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminReservationService {

  private final ReservationRepository reservationRepository;
  private final StoreRepository storeRepository;
  private final UserRepository userRepository;

  public Page<ReservationResDto> getAllStoreReservationList(Principal principal,
      Pageable pageable) {
    User user = userRepository.findByLoginId(principal.getName())
        .orElseThrow(NotExistUserException::new);
    List<Store> storeList = storeRepository.findAllByUser(user);

    return reservationRepository.findAllByStoreIn(storeList, pageable)
        .map(ReservationResDto::fromReservation);
  }

  public Page<ReservationResDto> getStoreReservationListByStatus(Long storeId,
      ReservationStatus status, Principal principal, Pageable pageable) {
    User user = userRepository.findByLoginId(principal.getName())
        .orElseThrow(NotExistUserException::new);
    Store store = storeRepository.findByUserAndId(user, storeId)
        .orElseThrow(CantFindStoreException::new);

    if (status != null) {
      return reservationRepository.findAllByStoreAndStatus(store, status, pageable)
          .map(ReservationResDto::fromReservation);
    } else {
      return reservationRepository.findAllByStore(store, pageable)
          .map(ReservationResDto::fromReservation);
    }
  }

  public Page<ReservationResDto> getStoreReservationListByDateAndStatus(Long storeId,
      LocalDate localDate, ReservationStatus status, Principal principal, Pageable pageable) {
    User user = userRepository.findByLoginId(principal.getName())
        .orElseThrow(NotExistUserException::new);
    Store store = storeRepository.findByUserAndId(user, storeId)
        .orElseThrow(CantFindStoreException::new);
    LocalDateTime startDate = localDate.atStartOfDay();
    LocalDateTime endDate = localDate.atTime(LocalTime.MAX);

    if (status != null) {
      return reservationRepository.findAllByStoreAndStatusAndReservationDateTimeBetween(store, status, startDate, endDate, pageable)
          .map(ReservationResDto::fromReservation);
    } else {
      return reservationRepository.findAllByStoreAndReservationDateTimeBetween(store, startDate, endDate, pageable)
          .map(ReservationResDto::fromReservation);
    }
  }

  public ReservationStatus changeReservationStatus(Long storeId,
      Long reservationId, ReservationStatus reservationStatus, ReservationStatus changeStatus,
      Principal principal) {
    //내가 가게의 사장인지 확인
    User user = userRepository.findByLoginId(principal.getName())
        .orElseThrow(NotExistUserException::new);
    Store store = storeRepository.findByUserIdAndId(user.getId(), storeId)
        .orElseThrow(NotExistStoreException::new);
    Reservation reservation = reservationRepository.findByStoreIdAndIdAndStatus(store.getId(),
        reservationId, reservationStatus).orElseThrow(NotExistReservationException::new);
    reservation.setStatus(changeStatus);
    reservationRepository.save(reservation);

    return reservation.getStatus();
  }
}
