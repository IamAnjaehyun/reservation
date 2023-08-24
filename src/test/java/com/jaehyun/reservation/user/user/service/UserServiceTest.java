package com.jaehyun.reservation.user.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

//@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private JwtTokenProvider jwtTokenProvider;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testJoin_Success() throws DuplicatedIdOrPhoneNumException {
    UserJoinDto userJoinDto = new UserJoinDto("testUser", "123456", "Test User", "1234567890");

    when(userRepository.existsByLoginIdOrPhoneNum(userJoinDto.getLoginId(),
        userJoinDto.getPhoneNum()))
        .thenReturn(false);
    when(passwordEncoder.encode(userJoinDto.getPassword())).thenReturn("encodedPassword");

    String loginId = userService.join(userJoinDto);

    assertNotNull(loginId);
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  public void testJoin_DuplicateIdOrPhoneNum() {
    UserJoinDto userJoinDto = new UserJoinDto("testUser", "123456", "Test User", "1234567890");

    when(userRepository.existsByLoginIdOrPhoneNum(userJoinDto.getLoginId(),
        userJoinDto.getPhoneNum()))
        .thenReturn(true);

    assertThrows(DuplicatedIdOrPhoneNumException.class, () -> userService.join(userJoinDto));
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  public void testLogin_IncorrectPassword() {
    UserLoginDto userLoginDto = new UserLoginDto("testUser", "123456");
    User user = new User();
    user.setLoginId(userLoginDto.getId());
    user.setPassword("encodedPassword");

    when(userRepository.findByLoginId(userLoginDto.getId())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())).thenReturn(false);

    assertThrows(IncorrectPassWordException.class, () -> userService.login(userLoginDto));
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  public void testLogin_NotExistUser() {
    UserLoginDto userLoginDto = new UserLoginDto("testUser", "123456");

    when(userRepository.findByLoginId(userLoginDto.getId())).thenReturn(Optional.empty());

    assertThrows(NotExistUserException.class, () -> userService.login(userLoginDto));
    verify(userRepository, never()).save(any(User.class));
  }

}