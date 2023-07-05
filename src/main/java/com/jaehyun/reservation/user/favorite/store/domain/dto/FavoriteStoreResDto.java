package com.jaehyun.reservation.user.favorite.store.domain.dto;

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
public class FavoriteStoreResDto {
  private Long id;
  private String name;
  private String description;
  private String location;
  private String phoneNum;
  private double averageRating;
  private double totalReviewCount;
}
