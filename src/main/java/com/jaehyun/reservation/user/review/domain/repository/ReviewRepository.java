package com.jaehyun.reservation.user.review.domain.repository;

import com.jaehyun.reservation.user.review.domain.entity.Review;
import com.jaehyun.reservation.user.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  boolean findByReservationId(Long id);
  boolean existsByReservationIdAndUser(Long reservationId, User user);
}
