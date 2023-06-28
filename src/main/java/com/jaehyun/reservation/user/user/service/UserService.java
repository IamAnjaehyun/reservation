package com.jaehyun.reservation.user.user.service;

import com.jaehyun.reservation.global.common.APIResponse;
import com.jaehyun.reservation.global.config.JwtTokenProvider;
import com.jaehyun.reservation.global.exception.impl.token.BadTokenException;
import com.jaehyun.reservation.global.exception.impl.user.DuplicatedIdOrPhoneNumException;
import com.jaehyun.reservation.global.exception.impl.user.IncorrectPassWordException;
import com.jaehyun.reservation.global.exception.impl.user.NotExistUserException;
import com.jaehyun.reservation.user.type.RoleType;
import com.jaehyun.reservation.user.user.domain.dto.UserJoinDto;
import com.jaehyun.reservation.user.user.domain.dto.UserLoginDto;
import com.jaehyun.reservation.user.user.domain.entity.User;
import com.jaehyun.reservation.user.user.domain.repository.UserRepository;
import java.security.Principal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final String API_NAME = "user";

  public APIResponse<String> join(UserJoinDto userJoinDto) throws DuplicatedIdOrPhoneNumException {
    if (userRepository.existsByLoginIdOrPhoneNum(userJoinDto.getLoginId(),
        userJoinDto.getPhoneNum())) {
      throw new DuplicatedIdOrPhoneNumException();
    }

    User user = User.builder()
        .loginId(userJoinDto.getLoginId())
        .phoneNum(userJoinDto.getPhoneNum())
        .password(passwordEncoder.encode(userJoinDto.getPassword()))
        .name(userJoinDto.getName())
        .roles(RoleType.USER)
        .build();
    userRepository.save(user);
    return APIResponse.create(API_NAME, user.getLoginId());
  }

  public APIResponse<String> login(UserLoginDto userLoginDto) {
    Optional<User> user = Optional.ofNullable(userRepository.findByLoginId(userLoginDto.getId())
        .orElseThrow(NotExistUserException::new));
    if (!passwordEncoder.matches(userLoginDto.getPassword(), user.get().getPassword())) {
      throw new IncorrectPassWordException();
    }

    return APIResponse.success(API_NAME,
        jwtTokenProvider.createToken(user.get().getLoginId(), user.get().getRoles()));
  }

  @Transactional
  public APIResponse<Void> quit(Principal principal) {
    Optional<User> user = Optional.ofNullable(userRepository.findByLoginId(principal.getName())
        .orElseThrow(NotExistUserException::new));
    userRepository.deleteById(user.get().getId());
    return APIResponse.delete();
  }

  public APIResponse<String> changeRole(Principal principal) {
    User user = userRepository.findByLoginId(principal.getName()).orElseThrow(() -> new BadTokenException());
    user.setRoles(RoleType.ADMIN);
    userRepository.saveAndFlush(user);
  }
}
