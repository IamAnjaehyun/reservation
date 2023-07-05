package com.jaehyun.reservation.admin.store.domain.dto;

import com.jaehyun.reservation.admin.store.domain.entity.Store;
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
public class StoreResDto {
  private String name; //매장 이름
  private String adminName; //사장 이름
  private String description; //매장 설명
  private String location; //매장 위치
  private String phoneNum; //매장 전화번호

  public static StoreResDto fromStore(Store store) {
    return StoreResDto.builder()
        .name(store.getName())
        .adminName(store.getUser().getName())
        .description(store.getDescription())
        .location(store.getLocation())
        .phoneNum(store.getPhoneNum())
        .build();
  }
}
