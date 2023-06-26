package com.jaehyun.reservation.user.reservation.domain.repository;

import com.jaehyun.reservation.user.reservation.domain.entity.Reservation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  List<Reservation> findAllByUserName(String userName);
}
