package com.jaehyun.reservation.user.review.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jaehyun.reservation.admin.store.entity.Store;
import com.jaehyun.reservation.global.entity.BaseTimeEntity;
import com.jaehyun.reservation.user.reservation.domain.Reservation;
import com.jaehyun.reservation.user.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Review extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reviewId; //리뷰 아이디

  @ManyToOne
  @JsonManagedReference
  @JoinColumn(name = "USER_ID")
  private User user; //유저 고유 id

  @ManyToOne
  @JsonManagedReference
  @JoinColumn(name = "STORE_ID")
  private Store store;//상점 아이디

  @OneToOne
  private Reservation reservation; //예약번호

  private String reviewText; //리뷰 텍스트
  private int starRating; //별점
}
