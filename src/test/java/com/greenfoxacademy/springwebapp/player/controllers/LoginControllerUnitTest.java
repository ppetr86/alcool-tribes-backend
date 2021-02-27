package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
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
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

public class LoginControllerUnitTest {

  private BindingResult bindingResult;
  private LoginController loginController;
  private PlayerService playerService;
  private TokenService tokenService;

  @Before
  public void setUp() {
    bindingResult = Mockito.mock(BindingResult.class);
    tokenService = Mockito.mock(TokenService.class);
    playerService = Mockito.mock(PlayerService.class);
    loginController = new LoginController(playerService, tokenService);
  }

  @Test
  public void postLoginShouldReturn200AndOkStatus() throws Exception {
    PlayerEntity playerEntity = new PlayerEntity("Mark", "markmark");
    PlayerTokenDTO fakePlayerDto = new PlayerTokenDTO("12345");
    PlayerRequestDTO requestDTO = new PlayerRequestDTO("Mark", "markmark");

    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    Mockito
        .when(tokenService.generateTokenToLoggedInPlayer(requestDTO))
        .thenReturn(fakePlayerDto);

    Mockito
        .when(playerService.findByUsernameAndPassword(requestDTO.getUsername(), requestDTO.getPassword()))
        .thenReturn(playerEntity);

    ResponseEntity<?> response = loginController.login(requestDTO, bindingResult);

    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assert.assertEquals("ok", ((PlayerTokenDTO) response.getBody()).getStatus());
    Assert.assertEquals("12345", (((PlayerTokenDTO) response.getBody()).getToken()));
  }

  @Test
  public void postLoginShould400ErrorStatusAndUsernameIsRequiredMessage() throws Exception {
    PlayerRequestDTO playerRequestDTO = new PlayerRequestDTO(null, "markmark");

    List<ObjectError> errorList = bindingResult.getAllErrors();
    errorList.add(new ObjectError("userDTO", "Username is required."));

    Mockito
        .when(bindingResult.getAllErrors())
        .thenReturn(errorList);

    ResponseEntity<?> response = loginController.login(playerRequestDTO, bindingResult);

    Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Assert.assertEquals("Username is required.", ((ErrorDTO) response.getBody()).getMessage());
  }

  @Test
  public void postLoginShould400ErrorStatusAndPasswordIsRequiredMessage() throws Exception {
    PlayerRequestDTO playerRequestDTO = new PlayerRequestDTO("Mark", null);

    List<ObjectError> errorList = bindingResult.getAllErrors();
    errorList.add(new ObjectError("userDTO", "Password is required."));

    Mockito
        .when(bindingResult.getAllErrors())
        .thenReturn(errorList);

    ResponseEntity<?> response = loginController.login(playerRequestDTO, bindingResult);

    Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Assert.assertEquals("Password is required.", ((ErrorDTO) response.getBody()).getMessage());
  }

  @Test
  public void postLoginShould400ErrorStatusAndPasswordHasContainAtLeast8LettersMessage() throws Exception {
    PlayerRequestDTO playerRequestDTO = new PlayerRequestDTO("Mark", "mark");

    List<ObjectError> errorList = bindingResult.getAllErrors();
    errorList.add(new ObjectError("userDTO", "Password has to contain at least 8 letters."));

    Mockito
        .when(bindingResult.getAllErrors())
        .thenReturn(errorList);

    ResponseEntity<?> response = loginController.login(playerRequestDTO, bindingResult);
    Assert.assertEquals("error", ((ErrorDTO) response.getBody()).getStatus());
    Assert.assertEquals("Password has to contain at least 8 letters.", ((ErrorDTO) response.getBody()).getMessage());
  }

  @Test
  public void postLoginShould400ErrorStatusAndUsernameAndPasswordAreRequiredMessage() throws Exception {
    PlayerRequestDTO playerRequestDTO = new PlayerRequestDTO();

    List<ObjectError> errorList = bindingResult.getAllErrors();
    errorList.add(new ObjectError("userDTO", "Username and password are required."));

    Mockito
        .when(bindingResult.getAllErrors())
        .thenReturn(errorList);

    ResponseEntity<?> response = loginController.login(playerRequestDTO, bindingResult);

    Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Assert.assertEquals("Username and password are required.", ((ErrorDTO) response.getBody()).getMessage());
  }

  @Test
  public void postLoginShould401ErrorStatusAndUsernameOrPasswordIsIncorrectMessageBecauseWrongUsername()
      throws Exception {
    PlayerRequestDTO requestDTO = new PlayerRequestDTO("BadMark", "markmark");
    PlayerEntity playerEntity = new PlayerEntity("Mark", "markmark");

    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    Mockito
        .when(playerService.findByUsernameAndPassword(requestDTO.getUsername(), requestDTO.getPassword()))
        .thenReturn(playerEntity);

    ResponseEntity<?> response = loginController.login(requestDTO, bindingResult);

    Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    Assert.assertEquals("Username or password is incorrect.", ((ErrorDTO) response.getBody()).getMessage());
  }

  @Test
  public void postLoginShould401ErrorStatusAndUsernameOrPasswordIsIncorrectMessageBecauseWrongPassword()
      throws Exception {

    PlayerRequestDTO requestDTO = new PlayerRequestDTO("Mark", "badPassword");
    PlayerEntity playerEntity = new PlayerEntity("Mark", "markmark");

    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    Mockito
        .when(playerService.findByUsernameAndPassword(requestDTO.getUsername(), requestDTO.getPassword()))
        .thenReturn(playerEntity);

    ResponseEntity<?> response = loginController.login(requestDTO, bindingResult);

    Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    Assert.assertEquals("Username or password is incorrect.", ((ErrorDTO) response.getBody()).getMessage());
  }
}