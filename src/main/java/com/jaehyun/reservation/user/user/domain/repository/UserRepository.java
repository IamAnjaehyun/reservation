package com.jaehyun.reservation.user.user.domain.repository;

import com.jaehyun.reservation.user.user.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
  Optional<User> findByLoginId(String name);
  User findByPhoneNum(String phoneNum);
  boolean existsByLoginIdOrPhoneNum(String loginId, String phoneNum);
  String deleteByLoginId(String loginId);

}
