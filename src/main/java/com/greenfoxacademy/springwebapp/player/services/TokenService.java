package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerDTO;

public interface TokenService {
  PlayerTokenDTO generateTokenToLoggedInPlayer(PlayerDTO playerDTO);
}
