package com.jaehyun.reservation.user.review.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jaehyun.reservation.admin.store.domain.entity.Store;
import com.jaehyun.reservation.global.entity.BaseTimeEntity;
import com.jaehyun.reservation.user.reservation.domain.entity.Reservation;
import com.jaehyun.reservation.user.user.domain.entity.User;
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
  private Long id; //리뷰 아이디

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
  private double starRating; //별점

  public void setReviewText(String reviewText) {
    this.reviewText = reviewText;
  }

  public void setStarRating(double starRating) {
    this.starRating = starRating;
  }
}
