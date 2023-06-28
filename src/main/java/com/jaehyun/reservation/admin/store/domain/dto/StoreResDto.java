package com.jaehyun.reservation.admin.store.domain.dto;

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
}
