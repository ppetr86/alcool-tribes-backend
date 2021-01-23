package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.UserDTO;

public interface LoginExceptionService {
  PlayerTokenDTO generateTokenToLoggedInPlayer(UserDTO userDTO);

}
