package com.jaehyun.reservation.user.reservation.domain.repository;

import com.jaehyun.reservation.admin.store.domain.entity.Store;
import com.jaehyun.reservation.user.reservation.domain.entity.Reservation;
import com.jaehyun.reservation.user.type.ReservationStatus;
import com.jaehyun.reservation.user.user.domain.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  List<Reservation> findAllByUser(User user);
  List<Reservation> findAllByStore(Store store);
  List<Reservation> findAllByStoreAndStatus(Store store, ReservationStatus status);
  List<Reservation> findAllByStoreAndStatusAndReservationDateTimeBetween(Store store, ReservationStatus status, LocalDateTime startTime, LocalDateTime endTime);

  Optional<Reservation> findByUserAndId(User user, Long id);

  boolean existsByReservationDateTimeAndUser(LocalDateTime localDateTime, User user);

  Optional<Reservation> findByStoreIdAndIdAndStatus(
      Long storeId, Long Id,ReservationStatus status);
}