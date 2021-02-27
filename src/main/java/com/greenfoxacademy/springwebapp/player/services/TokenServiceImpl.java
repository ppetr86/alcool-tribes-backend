package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {

  private final JwtProvider jwtProvider;

  @Override
  public PlayerTokenDTO generateTokenToLoggedInPlayer(PlayerEntity player) {
    String token = jwtProvider.generateToken(player);
    return new PlayerTokenDTO(token);
  }
}