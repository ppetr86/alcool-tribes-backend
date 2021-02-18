package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;

public interface TokenService {
  PlayerTokenDTO generateTokenToLoggedInPlayer(PlayerRequestDTO playerRequestDTO);
}
