package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerDTO;
import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImp implements TokenService{

  private PlayerEntityService playerEntityService;
  private JwtProvider jwtProvider;

  public TokenServiceImp(PlayerEntityService playerEntityService, JwtProvider jwtProvider) {
    this.playerEntityService = playerEntityService;
    this.jwtProvider = jwtProvider;
  }

  @Override
  public PlayerTokenDTO generateTokenToLoggedInPlayer(PlayerDTO playerDTO) {
    PlayerEntity loggedPlayer = playerEntityService.findByUsernameAndPassword(playerDTO.getUsername(), playerDTO.getPassword());
    String token = jwtProvider.generateToken(loggedPlayer.getUsername());
    return new PlayerTokenDTO(token);
  }
}
