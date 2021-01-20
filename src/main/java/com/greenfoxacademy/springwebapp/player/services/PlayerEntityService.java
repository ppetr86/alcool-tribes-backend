package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;

public interface PlayerEntityService {
  long countUsers();
  PlayerEntity saveUser(PlayerEntity playerEntity);
  PlayerEntity findByUsername(String username);
  PlayerEntity findByUsernameAndPassword(String username, String password);
}
