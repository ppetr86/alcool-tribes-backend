package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;

public interface PlayerService {
  PlayerEntity findByUsername(String username);

  PlayerEntity loginUser(PlayerEntity playerInput);
}
