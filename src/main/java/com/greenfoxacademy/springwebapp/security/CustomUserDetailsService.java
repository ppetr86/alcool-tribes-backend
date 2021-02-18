package com.greenfoxacademy.springwebapp.security;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private PlayerService playerService;

  @Override
  public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    PlayerEntity player = playerService.findByUsername(username);
    return CustomUserDetails.fromPlayerToCustomUserDetails(player);
  }
}
