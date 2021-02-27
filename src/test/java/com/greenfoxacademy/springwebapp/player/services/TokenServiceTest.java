package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TokenServiceTest {

  private TokenService tokenService;
  private JwtProvider mockJwtProvider;

  @Before
  public void setUp() {
    mockJwtProvider = Mockito.mock(JwtProvider.class);
    tokenService = new TokenServiceImpl(mockJwtProvider);
  }

  @Test
  public void generateTokenToLoggedInPlayerShouldGenerateTokenToLoggedPlayer() {
    PlayerEntity playerEntity = new PlayerEntity("Mark", "markmark");

    Mockito
        .when(mockJwtProvider.generateToken(playerEntity))
        .thenReturn("token");

    PlayerTokenDTO fakePlayerTokenDto = tokenService.generateTokenToLoggedInPlayer(playerEntity);

    Assert.assertEquals("ok", fakePlayerTokenDto.getStatus());
    Assert.assertEquals("token", fakePlayerTokenDto.getToken());
  }
}