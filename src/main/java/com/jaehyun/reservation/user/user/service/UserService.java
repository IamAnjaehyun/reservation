package com.jaehyun.reservation.user.user.service;

import com.jaehyun.reservation.global.config.JwtTokenProvider;
import com.jaehyun.reservation.global.exception.DuplicatedIdOrPhoneNumException;
import com.jaehyun.reservation.global.exception.IncorrectPassWordException;
import com.jaehyun.reservation.global.exception.NotExistUserException;
import com.jaehyun.reservation.user.user.domain.dto.UserJoinDto;
import com.jaehyun.reservation.user.user.domain.dto.UserLoginDto;
import com.jaehyun.reservation.user.user.domain.entity.User;
import com.jaehyun.reservation.user.user.domain.repository.UserRepository;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  public String join(UserJoinDto userJoinDto) throws DuplicatedIdOrPhoneNumException {
    String encodedPassword = passwordEncoder.encode(userJoinDto.getPassword());

    if (userRepository.existsByLoginIdOrPhoneNum(userJoinDto.getLoginId(),
        userJoinDto.getPhoneNum())) {
      throw new DuplicatedIdOrPhoneNumException();
    }

    User user = User.builder()
        .loginId(userJoinDto.getLoginId())
        .phoneNum(userJoinDto.getPhoneNum())
        .password(encodedPassword)
        .name(userJoinDto.getName())
        .roles(Collections.singletonList("USER"))
        .build();

    userRepository.save(user);
    return user.getId() + "번째 유저가 회원가입을 완료하였습니다.";
  }

  public String login(UserLoginDto userLoginDto) {
    Optional<User> user = userRepository.findByLoginId(userLoginDto.getId());

    if (user.isEmpty()) {
      throw new NotExistUserException();
    }
    if (!passwordEncoder.matches(userLoginDto.getPassword(), user.get().getPassword())) {
      throw new IncorrectPassWordException();
    }

    return jwtTokenProvider.createToken(user.get().getLoginId(), user.get().getRoles());
  }

}
