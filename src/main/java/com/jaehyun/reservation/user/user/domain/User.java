package com.jaehyun.reservation.user.user.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jaehyun.reservation.admin.store.entity.Store;
import com.jaehyun.reservation.global.entity.BaseTimeEntity;
import com.jaehyun.reservation.user.review.domain.Review;
import com.jaehyun.reservation.user.type.RoleType;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER")
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

  @JsonBackReference
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Review> reviewList;

  @JsonBackReference
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Store> storeList;
}
