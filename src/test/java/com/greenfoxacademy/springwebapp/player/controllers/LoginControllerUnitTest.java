package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.ErrorDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRequestDTO;
import com.greenfoxacademy.springwebapp.player.services.PlayerEntityService;
import com.greenfoxacademy.springwebapp.player.services.TokenService;
import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
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

  private LoginController loginController;
  private PlayerEntityService playerEntityService;
  private TokenService tokenService;

  @Before
  public void setUp() {
    tokenService = Mockito.mock(TokenService.class);
    playerEntityService = Mockito.mock(PlayerEntityService.class);
    loginController = new LoginController(playerEntityService, tokenService);
  }

  @Test
  public void postLoginShouldReturn200AndOkStatus() throws Exception{
    PlayerEntity playerEntity = new PlayerEntity("Mark", "markmark");
    PlayerTokenDTO fakePlayerDto = new PlayerTokenDTO("12345");

    PlayerRequestDTO playerRequestDTO = new PlayerRequestDTO("Mark", "markmark");

    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    Mockito.when(tokenService.generateTokenToLoggedInPlayer(playerRequestDTO)).thenReturn(fakePlayerDto);
    Mockito.when(playerEntityService.findByUsernameAndPassword(playerRequestDTO.getUsername(), playerRequestDTO.getPassword())).thenReturn(playerEntity);

    ResponseEntity<?> response = loginController.postLogin(playerRequestDTO, bindingResult);     //status ok

    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assert.assertEquals("ok", ((PlayerTokenDTO)response.getBody()).getStatus());
    Assert.assertEquals("12345", (((PlayerTokenDTO)response.getBody()).getToken()));
  }

  @Test
  public void postLoginShould400ErrorStatusAndUsernameIsRequiredMessage() throws Exception{
    PlayerRequestDTO playerRequestDTO = new PlayerRequestDTO(null, "markmark");

    BindingResult bindingResult = Mockito.mock(BindingResult.class);
    List<ObjectError> errorList = bindingResult.getAllErrors();
    errorList.add(new ObjectError("userDTO2", "Username is required."));

    Mockito.when(bindingResult.getAllErrors()).thenReturn(errorList);

    ResponseEntity<?> response = loginController.postLogin(playerRequestDTO, bindingResult);  //Username is required

    Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Assert.assertEquals("Username is required.", ((ErrorDTO)response.getBody()).getMessage());
  }

  @Test
  public void postLoginShould400ErrorStatusAndPasswordIsRequiredMessage() throws Exception{
    PlayerRequestDTO playerRequestDTO = new PlayerRequestDTO("Mark", null);

    BindingResult bindingResult = Mockito.mock(BindingResult.class);
    List<ObjectError> errorList = bindingResult.getAllErrors();
    errorList.add(new ObjectError("userDTO3", "Password is required."));

    Mockito.when(bindingResult.getAllErrors()).thenReturn(errorList);

    ResponseEntity<?> response = loginController.postLogin(playerRequestDTO, bindingResult);  //Password is required

    Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Assert.assertEquals("Password is required.", ((ErrorDTO)response.getBody()).getMessage());
  }

  @Test
  public void postLoginShould400ErrorStatusAndUsernameAndPasswordAreRequiredMessage() throws Exception{
    PlayerRequestDTO playerRequestDTO = new PlayerRequestDTO();

    BindingResult bindingResult = Mockito.mock(BindingResult.class);
    List<ObjectError> errorList = bindingResult.getAllErrors();
    errorList.add(new ObjectError("userDTO4", "Username and password are required."));

    Mockito.when(bindingResult.getAllErrors()).thenReturn(errorList);

    ResponseEntity<?> response = loginController.postLogin(playerRequestDTO, bindingResult);  //Username and password are required

    Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Assert.assertEquals("Username and password are required.", ((ErrorDTO)response.getBody()).getMessage());
  }

  @Test
  public void postLoginShould401ErrorStatusAndUsernameOrPasswordIsIncorrectMessageBecauseWrongUsername() throws Exception{
    PlayerRequestDTO playerRequestDTO = new PlayerRequestDTO("BadMark", "markmark");

    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    ResponseEntity<?> response = loginController.postLogin(playerRequestDTO, bindingResult);   //Username or password is incorrect (Bad username)

    Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    Assert.assertEquals("Username or password is incorrect.", ((ErrorDTO)response.getBody()).getMessage());
  }

  @Test
  public void postLoginShould401ErrorStatusAndUsernameOrPasswordIsIncorrectMessageBecauseWrongPassword() throws Exception{

    PlayerRequestDTO playerRequestDTO = new PlayerRequestDTO("Mark", "badPassword");

    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    ResponseEntity<?> response = loginController.postLogin(playerRequestDTO, bindingResult);   //Username or password is incorrect (Bad password)

    Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    Assert.assertEquals("Username or password is incorrect.", ((ErrorDTO)response.getBody()).getMessage());
  }
}