package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImp implements PlayerService {
  @Override
  public PlayerEntity findByUsername(String username) {
    return null;
  }

  @Override
  public PlayerEntity loginUser(PlayerEntity playerInput) {
    return null;
  }
}
