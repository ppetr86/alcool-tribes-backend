package com.greenfoxacademy.springwebapp.security;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

public class CustomUserDetails implements UserDetails {
  private PlayerEntity player;
  private String login;
  private String password;
  private KingdomEntity kingdom;
  private Boolean isVerified;
  private Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();

  public static CustomUserDetails fromPlayerToCustomUserDetails(PlayerEntity player) {
    CustomUserDetails details = new CustomUserDetails();
    details.player = player;
    details.login = player.getUsername();
    details.password = player.getPassword();
    details.kingdom = player.getKingdom();
    details.isVerified = player.getIsAccountVerified();
    details.grantedAuthorities = details.getAuthorities();
    details.grantedAuthorities.add(new SimpleGrantedAuthority(player.getRoleType().toString()));
    return details;
  }

  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    return grantedAuthorities;
  }

  public KingdomEntity getKingdom() {
    return kingdom;
  }

  public void setKingdom(KingdomEntity kingdom) {
    this.kingdom = kingdom;
  }

  public void setLogin(PlayerEntity player) {
    this.login = player.getUsername();
  }

  public PlayerEntity getPlayer() {
    return player;
  }

  public void setPlayer(PlayerEntity player) {
    this.player = player;
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
