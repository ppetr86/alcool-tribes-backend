package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;

import java.util.List;

public interface PlayerEntityService {
  List<PlayerEntity> findAllPlayer();
  long countPlayers();
  PlayerEntity saveUser(PlayerEntity playerEntity);
  PlayerEntity findByUsername(String username);
  PlayerEntity findByUsernameAndPassword(String username, String password);
}
