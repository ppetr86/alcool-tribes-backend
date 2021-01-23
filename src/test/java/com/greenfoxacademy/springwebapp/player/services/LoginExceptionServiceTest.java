package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.UserDTO;
import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class LoginExceptionServiceTest {

  private LoginExceptionService loginExceptionService;
  private PlayerEntityService playerEntityService;
  private JwtProvider mockJwtProvider;

  @Before
  public void init(){
    playerEntityService = Mockito.mock(PlayerEntityService.class);
    mockJwtProvider = Mockito.mock(JwtProvider.class);
    loginExceptionService = new LoginExceptionServiceImp(playerEntityService, mockJwtProvider);
  }

  @Test
  public void generateTokenToLoggedInPlayerShouldGenerateTokenToLoggedPlayer(){
    UserDTO userDTO = new UserDTO("Mark", "mark");
    PlayerEntity playerEntity = new PlayerEntity("Mark", "mark");

    Mockito.when(playerEntityService.findByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword())).thenReturn(playerEntity);
    Mockito.when(mockJwtProvider.generateToken(playerEntity.getUsername())).thenReturn(playerEntity.getUsername());

    PlayerTokenDTO fakePlayerTokenDTO = loginExceptionService.generateTokenToLoggedInPlayer(userDTO);

    Assert.assertEquals("ok", fakePlayerTokenDTO.getStatus());
  }

}