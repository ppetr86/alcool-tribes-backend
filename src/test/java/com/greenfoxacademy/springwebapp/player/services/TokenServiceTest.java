package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRequestDTO;
import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TokenServiceTest {

  private TokenService tokenService;
  private PlayerService playerService;
  private JwtProvider mockJwtProvider;

  @Before
  public void setUp() {
    playerService = Mockito.mock(PlayerServiceImp.class);
    mockJwtProvider = Mockito.mock(JwtProvider.class);
    tokenService = new TokenServiceImp(playerService, mockJwtProvider);
  }

  @Test
  public void generateTokenToLoggedInPlayerShouldGenerateTokenToLoggedPlayer() {
    PlayerEntity playerEntity = new PlayerEntity("Mark", "markmark");
    PlayerRequestDTO playerRequestDTO = new PlayerRequestDTO("Mark", "markmark");
    String username = playerRequestDTO.getUsername();
    String password = playerRequestDTO.getPassword();

    Mockito
      .when(playerService.findByUsernameAndPassword(username, password))
      .thenReturn(playerEntity);

    Mockito
      .when(mockJwtProvider.generateToken(playerEntity.getUsername()))
      .thenReturn("token");

    PlayerTokenDTO fakePlayerTokenDto = tokenService.generateTokenToLoggedInPlayer(playerRequestDTO);

    Assert.assertEquals("ok", fakePlayerTokenDto.getStatus());
    Assert.assertEquals("token", fakePlayerTokenDto.getToken());
  }
}