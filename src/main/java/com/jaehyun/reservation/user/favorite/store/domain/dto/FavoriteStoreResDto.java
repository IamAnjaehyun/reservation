package com.jaehyun.reservation.user.favorite.store.domain.dto;

import com.jaehyun.reservation.user.favorite.store.domain.entity.FavoriteStore;
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

  public static FavoriteStoreResDto fromFavoriteStoreResDto(FavoriteStore favoriteStore) {
    return FavoriteStoreResDto.builder()
        .id(favoriteStore.getStore().getId())
        .name(favoriteStore.getStore().getName())
        .description(favoriteStore.getStore().getDescription())
        .location(favoriteStore.getStore().getLocation())
        .phoneNum(favoriteStore.getStore().getPhoneNum())
        .averageRating(favoriteStore.getStore().getAverageRating())
        .totalReviewCount(favoriteStore.getStore().getTotalReviewCount())
        .build();
  }
}
