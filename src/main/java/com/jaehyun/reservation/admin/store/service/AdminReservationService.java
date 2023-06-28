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
import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;
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

  private final String API_NAME = "reservationList";

  public APIResponse<List<ReservationResDto>> getAllStoreReservationList(Principal principal) {
    return null;
  }

  public APIResponse<List<ReservationResDto>> getAllReservationList(Principal principal) {
    return null;
  }

  public APIResponse<List<ReservationResDto>> getStoreReservationListByStatus(Principal principal) {
    return null;
  }

  public APIResponse<List<ReservationResDto>> getStoreReservationListByDateAndStatus(
      Principal principal) {
    return null;
  }

  public APIResponse<String> changeReservationStatus(Long storeId,
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

    return APIResponse.success("reservationStatus", reservation.getStatus().toString());
  }
}