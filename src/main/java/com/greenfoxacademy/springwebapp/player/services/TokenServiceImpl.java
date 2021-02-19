package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

  private final PlayerService playerService;
  private final JwtProvider jwtProvider;

  public TokenServiceImpl(PlayerService playerService, JwtProvider jwtProvider) {
    this.playerService = playerService;
    this.jwtProvider = jwtProvider;
  }

  @Override
  public PlayerTokenDTO generateTokenToLoggedInPlayer(PlayerRequestDTO playerRequestDTO) {
    PlayerEntity loggedPlayer = playerService.findByUsernameAndPassword(playerRequestDTO.getUsername(), playerRequestDTO.getPassword());
    String token = jwtProvider.generateToken(loggedPlayer);
    return new PlayerTokenDTO(token);
  }
}
