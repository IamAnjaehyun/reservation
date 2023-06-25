package com.jaehyun.reservation.admin.store.domain.repository;

import com.jaehyun.reservation.admin.store.domain.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

}
