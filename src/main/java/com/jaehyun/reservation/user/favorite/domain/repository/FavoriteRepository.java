package com.jaehyun.reservation.user.favorite.domain.repository;

import com.jaehyun.reservation.user.favorite.domain.Favorite;
import com.jaehyun.reservation.user.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
  Favorite findByUser(User user);
}
