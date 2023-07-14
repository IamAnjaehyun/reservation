package com.jaehyun.reservation.user.reservation.domain.repository;

import com.jaehyun.reservation.admin.store.domain.entity.Store;
import com.jaehyun.reservation.user.reservation.domain.entity.Reservation;
import com.jaehyun.reservation.user.type.ReservationStatus;
import com.jaehyun.reservation.user.user.domain.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  List<Reservation> findAllByUser(User user);
  List<Reservation> findAllByStatus(ReservationStatus status);
  List<Reservation> findAllByReservationDateTimeBeforeAndStatus(LocalDateTime dateTime, ReservationStatus status);

  Page<Reservation> findAllByStore(Store store, Pageable pageable);

  Page<Reservation> findAllByStoreIn(List<Store> storeList, Pageable pageable);

  Page<Reservation> findAllByStoreAndStatus(Store store, ReservationStatus status,
      Pageable pageable);

  Page<Reservation> findAllByStoreAndStatusAndReservationDateTimeBetween(Store store,
      ReservationStatus status, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

  Page<Reservation> findAllByStoreAndReservationDateTimeBetween(Store store,
      LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

  Optional<Reservation> findByUserAndId(User user, Long id);
  Optional<Reservation> findByIdAndStatus(Long id, ReservationStatus status);

  boolean existsByReservationDateTimeAndUser(LocalDateTime localDateTime, User user);


  Optional<Reservation> findByStoreIdAndIdAndStatus(
      Long storeId, Long Id, ReservationStatus status);
}
