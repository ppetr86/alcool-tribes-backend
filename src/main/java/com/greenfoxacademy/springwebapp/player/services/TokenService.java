package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRequestDTO;

public interface TokenService {
  PlayerTokenDTO generateTokenToLoggedInPlayer(PlayerRequestDTO playerRequestDTO);
}
