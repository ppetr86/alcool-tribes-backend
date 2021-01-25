package com.greenfoxacademy.springwebapp.security;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

  PlayerService playerService;

  public CustomUserDetailsService(PlayerService playerService) {
    this.playerService = playerService;
  }

  @Override
  public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    PlayerEntity playerEntity = playerService.findByUsername(username);
    return CustomUserDetails.fromPlayerToCustomUserDetails(playerEntity);
  }
}
