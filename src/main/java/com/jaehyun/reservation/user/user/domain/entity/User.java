package com.jaehyun.reservation.user.user.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jaehyun.reservation.admin.store.domain.entity.Store;
import com.jaehyun.reservation.global.entity.BaseTimeEntity;
import com.jaehyun.reservation.user.favorite.domain.Favorite;
import com.jaehyun.reservation.user.review.domain.entity.Review;
import com.jaehyun.reservation.user.type.RoleType;
import java.io.Serializable;
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
import javax.persistence.OneToOne;
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
public class User extends BaseTimeEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; //유저 id

  @Column(unique = true)
  private String loginId; //로그인용 아이디

  @Column(unique = true)
  private String phoneNum;//유저 휴대폰 번호 겸 아이디

  private String password;//유저 비밀번호

  private String name; //유저 이름

  private String refreshToken;

  @Enumerated(EnumType.STRING)
  private RoleType roles; //ADMIN or USER

  @JsonBackReference
  @OneToMany(mappedBy = "user")
  private List<Review> reviewList; //회원 탈퇴해도 리뷰는 남아있음

  @JsonBackReference
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Store> storeList; //사장 회원탈퇴하면 상점 사라져야함

  @OneToOne
  private Favorite favoriteList; //자주가는 식당 목록

  public void setRoles(RoleType roles) {
    this.roles = roles;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
