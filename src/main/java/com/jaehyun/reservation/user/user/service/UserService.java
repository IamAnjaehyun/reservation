package com.jaehyun.reservation.user.user.service;

import com.jaehyun.reservation.global.common.APIResponse;
import com.jaehyun.reservation.global.config.JwtTokenProvider;
import com.jaehyun.reservation.global.entity.dto.Token;
import com.jaehyun.reservation.global.exception.impl.user.DuplicatedIdOrPhoneNumException;
import com.jaehyun.reservation.global.exception.impl.user.IncorrectPassWordException;
import com.jaehyun.reservation.global.exception.impl.user.NotExistUserException;
import com.jaehyun.reservation.user.type.RoleType;
import com.jaehyun.reservation.user.user.domain.dto.UserJoinDto;
import com.jaehyun.reservation.user.user.domain.dto.UserLoginDto;
import com.jaehyun.reservation.user.user.domain.entity.User;
import com.jaehyun.reservation.user.user.domain.repository.UserRepository;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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

  public String join(UserJoinDto userJoinDto) throws DuplicatedIdOrPhoneNumException {
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
    return user.getLoginId();
  }

  public Token login(UserLoginDto userLoginDto) {
    User user = userRepository.findByLoginId(userLoginDto.getId())
        .orElseThrow(NotExistUserException::new);
    if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
      throw new IncorrectPassWordException();
    }
    Token token =jwtTokenProvider.createToken(user.getLoginId(), user.getRoles());
    user.setRefreshToken(token.getRefreshToken());
    userRepository.save(user);
    return token;
  }

  @Transactional
  public void quit(Principal principal) {
    User user = findUserByPrincipal(principal);
    userRepository.deleteById(user.getId());
    APIResponse.delete();
  }

  public String changeRole(Principal principal) {
    User user = findUserByPrincipal(principal);
    user.setRoles(RoleType.ADMIN);
    userRepository.saveAndFlush(user);
    return RoleType.ADMIN.getKey();
  }

  @Cacheable(value = "user")
  public User findUserByPrincipal(Principal principal){
    return userRepository.findByLoginId(principal.getName()).orElseThrow(NotExistUserException::new);
  }
}
