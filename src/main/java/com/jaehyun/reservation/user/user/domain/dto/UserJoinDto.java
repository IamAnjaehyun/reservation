package com.jaehyun.reservation.user.user.domain.dto;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class UserJoinDto {
  private String loginId; //로그인용 아이디

  private String phoneNum;//유저 휴대폰 번호 겸 아이디

  private String password;//유저 비밀번호

  private String name; //유저 이름
}
