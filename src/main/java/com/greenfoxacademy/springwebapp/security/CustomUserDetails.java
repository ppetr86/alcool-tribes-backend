package com.greenfoxacademy.springwebapp.security;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

public class CustomUserDetails implements UserDetails {
  private String login;
  private String password;
  private KingdomEntity kingdom;
  private Collection<? extends GrantedAuthority> grantedAuthorities;

  public static CustomUserDetails fromPlayerToCustomUserDetails(PlayerEntity player) {
    CustomUserDetails details = new CustomUserDetails();
    details.login = player.getUsername();
    details.password = player.getPassword();
    details.kingdom = player.getKingdom();
    details.grantedAuthorities = details.getAuthorities();//returning empty authorities since we dont use roles
    return details;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return new HashSet<GrantedAuthority>(); //returns empty authorities since we dont use roles
  }

  public KingdomEntity getKingdom() {
    return kingdom;
  }

  public void setKingdom(KingdomEntity kingdom) {
    this.kingdom = kingdom;
  }

  public void setLogin(PlayerEntity player){
    this.login = player.getUsername() ;}

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
