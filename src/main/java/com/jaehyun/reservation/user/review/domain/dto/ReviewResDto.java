package com.jaehyun.reservation.user.review.domain.dto;

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
public class ReviewResDto {
  private Long reviewId;
  private Long storeId;
  private String storeName;
  private Long userId;
  private String userName;
  private double stars;
  private String reviewText;
}
