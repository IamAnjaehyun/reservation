package com.jaehyun.reservation.user.reservation.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.jaehyun.reservation.admin.store.entity.Store;
import com.jaehyun.reservation.global.entity.BaseTimeEntity;
import com.jaehyun.reservation.user.review.domain.Review;
import com.jaehyun.reservation.user.type.ReservationStatus;
import com.jaehyun.reservation.user.user.domain.User;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "RESERVATION")
public class Reservation extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reservationId; //예약 id

  @ManyToOne
  @JoinColumn(name = "USER_ID")
  private User user; //유저 고유 id

  @ManyToOne
  @JoinColumn(name = "STORE_ID")
  private Store store; //상점 id

  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
  private LocalDateTime reservationDateTime; //예약 날짜 및 시간

  private int reservationPeopleNum; //예약 인원수

  @Enumerated(EnumType.STRING)
  private ReservationStatus status = ReservationStatus.DEFAULT; //예약 승인 여부 (기본 PLEASE_WAIT)

  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
  private LocalDateTime comeCheckTime; //예약 날짜 및 시간

  @OneToOne(cascade = CascadeType.ALL)
  private Review review; //예약 한 후 상점을 이용한 회원이라면 리뷰작성이 가능
}
