package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;

public interface PlayerEntityService {
  PlayerEntity findByUsername(String username);
  PlayerEntity findByUsernameAndPassword(String username, String password);
}
