package com.jaehyun.reservation.user.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
  USER("USER", "일반 유저"),
  ADMIN("ADMIN", "상점 주인");

  private final String key;
  private final String title;

}
