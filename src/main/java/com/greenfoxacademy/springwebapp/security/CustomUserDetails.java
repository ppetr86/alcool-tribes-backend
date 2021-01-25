package com.greenfoxacademy.springwebapp.security;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import java.util.Collection;
import java.util.HashSet;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
  private String login;
  private String password;
  private Collection<? extends GrantedAuthority> grantedAuthorities;

  public static CustomUserDetails fromPlayerToCustomUserDetails(PlayerEntity playerEntity){
    CustomUserDetails details = new CustomUserDetails();
    details.login = playerEntity.getUsername();
    details.password = playerEntity.getPassword();
    details.grantedAuthorities= details.getAuthorities();//returning empty authorities since we dont use roles
    return details;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return new HashSet<GrantedAuthority>(); //returns empty authorities since we dont use roles
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return login;
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