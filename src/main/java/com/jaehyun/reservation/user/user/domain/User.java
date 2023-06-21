package com.jaehyun.reservation.user.user.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jaehyun.reservation.admin.store.entity.Store;
import com.jaehyun.reservation.global.entity.BaseTimeEntity;
import com.jaehyun.reservation.user.review.domain.Review;
import com.jaehyun.reservation.user.type.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId; //유저 id

  private String userLoginId; //로그인용 아이디

  @Column(unique = true)
  private String userPhoneNum;//유저 휴대폰 번호 겸 아이디

  private String userPassword;//유저 비밀번호

  private String userName; //유저 이름

  @Enumerated(EnumType.STRING)
  private RoleType roleType;

  @Builder.Default
  @JsonBackReference
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Review> reviewList;

  @Builder.Default
  @JsonBackReference
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Store> storeList;
}
