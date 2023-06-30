package com.jaehyun.reservation.admin.store.domain.repository;

import com.jaehyun.reservation.admin.store.domain.entity.Store;
import com.jaehyun.reservation.user.user.domain.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
  boolean existsByName(String name);
  Optional<Store> findByName(String name);
  Optional<Store> findByIdAndUserId(Long id, Long userId);
  List<Store> findAllByUser(User user);
  Optional<Store> findByUserAndId(User user, Long id);
}
