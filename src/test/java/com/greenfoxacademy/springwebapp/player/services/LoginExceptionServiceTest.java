package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class LoginExceptionServiceTest {

  private LoginExceptionService loginExceptionService;

  @Before
  public void init(){
    PlayerEntityService playerEntityService = Mockito.mock(PlayerEntityService.class);
    JwtProvider mockJwtProvider = Mockito.mock(JwtProvider.class);
    loginExceptionService = new LoginExceptionServiceImp(playerEntityService, mockJwtProvider);
  }

  @Test
  public void loginExceptionMethodShouldReturnCorrectErrorMessageDTODetails(){
    ErrorMessageDTO fakeErrorMessageDTO = loginExceptionService.loginExceptions("error", "hello");

    Assert.assertEquals("error", fakeErrorMessageDTO.getStatus());
    Assert.assertEquals("hello", fakeErrorMessageDTO.getMessage());
  }

}