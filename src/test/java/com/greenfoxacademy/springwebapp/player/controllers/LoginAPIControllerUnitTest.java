package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.UserDTO;
import com.greenfoxacademy.springwebapp.player.services.LoginExceptionService;
import com.greenfoxacademy.springwebapp.player.services.LoginExceptionServiceImp;
import com.greenfoxacademy.springwebapp.player.services.PlayerEntityService;
import com.greenfoxacademy.springwebapp.player.services.PlayerEntityServiceImp;
import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class LoginAPIControllerUnitTest {

  private LoginAPIController loginAPIController;
  private PlayerEntityService playerEntityService;
  private LoginExceptionService loginExceptionService;

  @Before
  public void setUp() {
    playerEntityService = Mockito.mock(PlayerEntityService.class);
    loginExceptionService = Mockito.mock(LoginExceptionService.class);
    loginAPIController = new LoginAPIController(playerEntityService, loginExceptionService);
  }

  @Test
  public void postLoginShouldReturnTokenDTOWithTokenAndOkMessage() throws Exception{

    PlayerEntity playerEntity = new PlayerEntity("Mark", "mark");
    UserDTO userDTO = new UserDTO("Mark", "mark");
    PlayerTokenDTO fakePlayerDto = new PlayerTokenDTO("ok", playerEntity.getUsername());
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    Mockito.when(loginExceptionService.generateTokenToLoggedInPlayer(userDTO)).thenReturn(fakePlayerDto);
    Mockito.when(playerEntityService.findByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword())).thenReturn(playerEntity);
    Mockito.when(playerEntityService.countPlayers()).thenReturn(1L);

    ResponseEntity<?> response = loginAPIController.postLogin(userDTO, bindingResult);

    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assert.assertEquals("ok", ((PlayerTokenDTO)response.getBody()).getStatus());
  }

}