package com.jaehyun.reservation.user.reservation.domain.repository;

import com.jaehyun.reservation.user.reservation.domain.entity.Reservation;
import com.jaehyun.reservation.user.user.domain.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  List<Reservation> findAllByUser(User user);
  Optional<Reservation> findByUserAndId(User user, Long id);
}
