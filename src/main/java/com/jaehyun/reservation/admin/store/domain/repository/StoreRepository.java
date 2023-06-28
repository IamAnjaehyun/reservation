package com.jaehyun.reservation.admin.store.domain.repository;

import com.jaehyun.reservation.admin.store.domain.entity.Store;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
  boolean existsByName(String name);
  Optional<Store> findByName(String name);
  Optional<Store> findByIdAndUserId(Long id, Long userId);
}
