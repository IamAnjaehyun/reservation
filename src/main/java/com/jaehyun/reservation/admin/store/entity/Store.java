package com.jaehyun.reservation.admin.store.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jaehyun.reservation.global.entity.BaseTimeEntity;
import com.jaehyun.reservation.user.reservation.domain.Reservation;
import com.jaehyun.reservation.user.review.domain.Review;
import com.jaehyun.reservation.user.user.domain.User;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "STORE")
public class Store extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long storeId;//매장 번호

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "USER_ID")
  private User user; //사장인지 확인하기 위한 사장 핸드폰번호(여기선 ID로 쓰임)

  @Column(unique = true)
  private String storeName; //매장 이름

  private String storeDescription; //매장 설명

  private String storeLocation; //매장 위치

  private String storePhoneNum; //매장 전화번호

  //평점 0.0~5.0 평점 나누기 평가자 수
  @Column(columnDefinition = "DOUBLE(3, 1) CHECK (average_rating >= 0.0 AND average_rating <= 5.0)")
  private double averageRating; //평점
  private Long totalRating; //총 별점
  private int totalReviewCount; //별점 준 인원수

  @JsonBackReference
  @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
  private List<Reservation> reservationList; //예약 목록

  @JsonBackReference
  @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
  private List<Review> reviewList; //리뷰 목록
}
