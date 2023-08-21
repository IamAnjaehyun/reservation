package com.jaehyun.reservation.admin.store.domain.dto;

import com.jaehyun.reservation.admin.store.domain.entity.Store;
import java.io.Serializable;
import javax.persistence.Column;
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
public class StoreViewDto implements Serializable {
  private Long storeId; //매장 고유 번호
  private String name; //매장 이름

  private String description; //매장 설명

  private String location; //매장 위치

  private String phoneNum; //매장 전화번호

  //평점 0.0~5.0 평점 나누기 평가자 수
  private double averageRating; //평점

  private int totalReviewCount; //별점 준 인원수
  private int favoriteCount; //즐겨찾기 수

  public static StoreViewDto fromStore(Store store) {
    return StoreViewDto.builder()
        .storeId(store.getId())
        .name(store.getName())
        .description(store.getDescription())
        .location(store.getLocation())
        .favoriteCount(store.getFavoriteCount())
        .phoneNum(store.getPhoneNum())
        .averageRating(store.getAverageRating())
        .totalReviewCount(store.getTotalReviewCount())
        .build();
  }
}
