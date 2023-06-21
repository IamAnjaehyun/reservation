package com.jaehyun.reservation.user.review.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jaehyun.reservation.admin.store.entity.Store;
import com.jaehyun.reservation.global.entity.BaseTimeEntity;
import com.jaehyun.reservation.user.reservation.domain.Reservation;
import com.jaehyun.reservation.user.user.domain.User;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "REVIEW")
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
