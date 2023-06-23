package com.jaehyun.reservation.global.service;

import com.jaehyun.reservation.user.type.RoleType;
import com.jaehyun.reservation.user.user.domain.entity.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtUserDetails implements UserDetails {

  private User user;

  public JwtUserDetails(User user) {
    super();
    this.user = user;
  }


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    RoleType role = user.getRoles();
    List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();

    authorities.add(new SimpleGrantedAuthority(role.toString()));
    return authorities;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getLoginId();
  }


  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}