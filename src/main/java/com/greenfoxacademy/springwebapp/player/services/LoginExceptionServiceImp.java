package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.LoginStatusOkDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LoginExceptionServiceImp implements LoginExceptionService {

  private PlayerEntityService playerEntityService;

  public LoginExceptionServiceImp(PlayerEntityService playerEntityService) {
    this.playerEntityService = playerEntityService;
  }


  @Override
  public ErrorMessageDTO loginExceptions(String error, String msg) {
    ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO();
    errorMessageDTO.setStatus(error);
    errorMessageDTO.setMessage(msg);
    return errorMessageDTO;
  }

  @Override
  public LoginStatusOkDTO loginStatusOk(String ok, String token) {
    LoginStatusOkDTO loginStatusOkDTO = new LoginStatusOkDTO();
    loginStatusOkDTO.setStatus(ok);
    loginStatusOkDTO.setToken(token);
    return loginStatusOkDTO;
  }
}
