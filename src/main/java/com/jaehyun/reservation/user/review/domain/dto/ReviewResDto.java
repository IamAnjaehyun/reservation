package com.jaehyun.reservation.user.review.domain.dto;

import com.jaehyun.reservation.user.review.domain.entity.Review;
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

  public static ReviewResDto fromReviewResDto(Review review) {
    return ReviewResDto.builder()
        .stars(review.getStarRating())
        .reviewId(review.getId())
        .userId(review.getUser().getId())
        .userName(review.getUser().getName())
        .reviewText(review.getReviewText())
        .storeId(review.getStore().getId())
        .storeName(review.getStore().getName())
        .build();
  }
}
