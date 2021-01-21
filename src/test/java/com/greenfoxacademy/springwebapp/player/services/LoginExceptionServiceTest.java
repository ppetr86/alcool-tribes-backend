package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.dtos.ErrorMessageDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginExceptionServiceTest {

  private LoginExceptionService loginExceptionService;

  @Before
  public void init(){
    loginExceptionService = new LoginExceptionServiceImp();
  }

  @Test
  public void loginExceptionMethodShouldReturnCorrectErrorMessageDTODetails(){
    Assert.assertEquals("error", loginExceptionService.loginExceptions("error", "hello").getStatus());
    Assert.assertEquals("hello", loginExceptionService.loginExceptions("error", "hello").getMessage());
  }

}