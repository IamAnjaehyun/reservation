package com.jaehyun.reservation.admin.store.domain.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jaehyun.reservation.global.entity.BaseTimeEntity;
import com.jaehyun.reservation.user.reservation.domain.entity.Reservation;
import com.jaehyun.reservation.user.review.domain.entity.Review;
import com.jaehyun.reservation.user.user.domain.entity.User;
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
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "STORE")
public class Store extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;//매장 번호

  @JsonManagedReference
  @ManyToOne
  @JoinColumn(name = "USER_ID")
  private User user; //사장인지 확인하기 위한 사장 핸드폰번호(여기선 ID로 쓰임)

  @Column(unique = true)
  private String name; //매장 이름

  private String description; //매장 설명

  private String location; //매장 위치

  private String phoneNum; //매장 전화번호

  //평점 0.0~5.0 평점 나누기 평가자 수
  @Column(columnDefinition = "DOUBLE(3, 1) CHECK (average_rating >= 0.0 AND average_rating <= 5.0)")
  private double averageRating; //평점
  private Long totalRating; //총 별점
  private int totalReviewCount; //별점 준 인원수

  private int favoriteCount; //좋아요 받은 수

  @JsonBackReference
  @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
  private List<Reservation> reservationList; //예약 목록

  @JsonBackReference
  @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
  private List<Review> reviewList; //리뷰 목록
}
