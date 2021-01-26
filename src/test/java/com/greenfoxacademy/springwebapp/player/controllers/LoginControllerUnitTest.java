package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerDTO;
import com.greenfoxacademy.springwebapp.player.services.PlayerEntityService;
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
  public void postLoginShouldReturnTokenDTOWithTokenAndOkMessage() throws Exception{

    PlayerEntity playerEntity = new PlayerEntity("Mark", "mark");
    PlayerTokenDTO fakePlayerDto = new PlayerTokenDTO(playerEntity.getUsername());

    PlayerDTO playerDTO = new PlayerDTO("Mark", "mark");
    PlayerDTO playerDTO2 = new PlayerDTO(null, "mark");
    PlayerDTO playerDTO3 = new PlayerDTO("Mark", null);
    PlayerDTO playerDTO4 = new PlayerDTO();
    PlayerDTO playerDTO5 = new PlayerDTO("BadMark", "mark");
    PlayerDTO playerDTO6 = new PlayerDTO("Mark", "badPassword");

    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    BindingResult bindingResult2 = Mockito.mock(BindingResult.class);
    List<ObjectError> errorList = bindingResult2.getAllErrors();
    errorList.add(new ObjectError("userDTO2", "Username is required."));

    BindingResult bindingResult3 = Mockito.mock(BindingResult.class);
    List<ObjectError> errorList2 = bindingResult3.getAllErrors();
    errorList2.add(new ObjectError("userDTO3", "Password is required."));

    BindingResult bindingResult4 = Mockito.mock(BindingResult.class);
    List<ObjectError> errorList3 = bindingResult4.getAllErrors();
    errorList3.add(new ObjectError("userDTO4", "Username and password are required."));

    Mockito.when(bindingResult2.getAllErrors()).thenReturn(errorList);
    Mockito.when(bindingResult3.getAllErrors()).thenReturn(errorList2);
    Mockito.when(bindingResult4.getAllErrors()).thenReturn(errorList3);
    Mockito.when(tokenService.generateTokenToLoggedInPlayer(playerDTO)).thenReturn(fakePlayerDto);
    Mockito.when(playerEntityService.findByUsernameAndPassword(playerDTO.getUsername(), playerDTO.getPassword())).thenReturn(playerEntity);

    ResponseEntity<?> response = loginController.postLogin(playerDTO, bindingResult);     //status ok
    ResponseEntity<?> response2 = loginController.postLogin(playerDTO2, bindingResult2);  //Username is required
    ResponseEntity<?> response3 = loginController.postLogin(playerDTO3, bindingResult3);  //Password is required
    ResponseEntity<?> response4 = loginController.postLogin(playerDTO4, bindingResult4);  //Username and password are required
    ResponseEntity<?> response5 = loginController.postLogin(playerDTO5, bindingResult);   //Username or password is incorrect (Bad username)
    ResponseEntity<?> response6 = loginController.postLogin(playerDTO6, bindingResult);   //Username or password is incorrect (Bad password)

    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assert.assertEquals("ok", ((PlayerTokenDTO)response.getBody()).getStatus());

    Assert.assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
    Assert.assertEquals("Username is required.", ((ErrorMessageDTO)response2.getBody()).getMessage());

    Assert.assertEquals(HttpStatus.BAD_REQUEST, response3.getStatusCode());
    Assert.assertEquals("Password is required.", ((ErrorMessageDTO)response3.getBody()).getMessage());

    Assert.assertEquals(HttpStatus.BAD_REQUEST, response4.getStatusCode());
    Assert.assertEquals("Username and password are required.", ((ErrorMessageDTO)response4.getBody()).getMessage());

    Assert.assertEquals(HttpStatus.UNAUTHORIZED, response5.getStatusCode());
    Assert.assertEquals("Username or password is incorrect.", ((ErrorMessageDTO)response5.getBody()).getMessage());

    Assert.assertEquals(HttpStatus.UNAUTHORIZED, response6.getStatusCode());
    Assert.assertEquals("Username or password is incorrect.", ((ErrorMessageDTO)response6.getBody()).getMessage());
  }
}