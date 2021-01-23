package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.UserDTO;

public interface LoginExceptionService {
  ErrorMessageDTO loginExceptions(String error, String msg);
  PlayerTokenDTO generateTokenToLoggedInPlayer(UserDTO userDTO);

}
