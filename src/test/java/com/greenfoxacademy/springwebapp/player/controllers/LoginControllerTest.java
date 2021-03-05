package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IncorrectUsernameOrPwdException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotVerifiedRegistrationException;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import com.greenfoxacademy.springwebapp.player.services.TokenService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class LoginControllerTest {

  private LoginController loginController;
  private PlayerService playerService;
  private TokenService tokenService;

  @Before
  public void setUp() {
    tokenService = Mockito.mock(TokenService.class);
    playerService = Mockito.mock(PlayerService.class);
    loginController = new LoginController(playerService);
  }

  @Test
  public void postLoginShouldReturn200AndOkStatus()
      throws Exception, NotVerifiedRegistrationException, IncorrectUsernameOrPwdException {
    PlayerEntity entity = new PlayerEntity("Mark", "markmark");
    PlayerTokenDTO tokenDTO = new PlayerTokenDTO("12345");
    PlayerRequestDTO requestDTO = new PlayerRequestDTO("Mark", "markmark");
    Mockito
        .when(tokenService.generateTokenToLoggedInPlayer(entity))
        .thenReturn(tokenDTO);
    Mockito
        .when(playerService.findByUsernameAndPassword(requestDTO.getUsername(), requestDTO.getPassword()))
        .thenReturn(entity);
    ResponseEntity<PlayerTokenDTO> response = loginController.login(requestDTO);
    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test(expected = IncorrectUsernameOrPwdException.class)
  public void loginShould401_WrongPasswordAndOrUsername()
      throws IncorrectUsernameOrPwdException, NotVerifiedRegistrationException {
    PlayerRequestDTO requestDTO = new PlayerRequestDTO("Mark", "badPassword");
    Mockito
        .when(playerService.loginPlayer(requestDTO))
        .thenThrow(IncorrectUsernameOrPwdException.class);
    ResponseEntity<?> response = loginController.login(requestDTO);
    Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    Assert.assertEquals("Username or password is incorrect.", ((ErrorDTO) response.getBody()).getMessage());
  }

  @Test(expected = NotVerifiedRegistrationException.class)
  public void loginShould401_NotVerifiedPlayer()
      throws IncorrectUsernameOrPwdException, NotVerifiedRegistrationException {
    PlayerRequestDTO requestDTO = new PlayerRequestDTO("Mark", "badPassword");
    Mockito
        .when(playerService.loginPlayer(requestDTO))
        .thenThrow(NotVerifiedRegistrationException.class);
    ResponseEntity<?> response = loginController.login(requestDTO);
    Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    Assert.assertEquals("Not verified username.", ((ErrorDTO) response.getBody()).getMessage());
  }
}