package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.LoginStatusOkDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.UserDTO;

public interface LoginExceptionService {
  ErrorMessageDTO loginExceptions(String error, String msg);
  LoginStatusOkDTO loginStatusOk(String ok, String token);

}
