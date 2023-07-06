package com.jaehyun.reservation.user.reservation.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.jaehyun.reservation.admin.store.domain.entity.Store;
import com.jaehyun.reservation.user.reservation.domain.entity.Reservation;
import com.jaehyun.reservation.user.type.ReservationStatus;
import java.time.LocalDateTime;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResDto {
  private Long reservationId;

  private Long userId;
  private String userName;

  private Long storeId;
  private String storeName;
  private ReservationStatus reservationStatus;

  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
  private LocalDateTime reservationDateTime; //예약 날짜 및 시간

  private int reservationPeopleNum; //예약 인원수

  public static ReservationResDto fromReservation(Reservation reservation) {
    return ReservationResDto.builder()
        .storeId(reservation.getStore().getId())
        .userId(reservation.getUser().getId())
        .reservationId(reservation.getId())
        .reservationStatus(reservation.getStatus())
        .reservationPeopleNum(reservation.getReservationPeopleNum())
        .reservationDateTime(reservation.getReservationDateTime())
        .storeName(reservation.getStore().getName())
        .userName(reservation.getUser().getName())
        .build();
  }
}
