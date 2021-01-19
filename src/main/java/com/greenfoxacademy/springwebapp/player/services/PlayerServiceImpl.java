package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.repositories.PlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService{
  PlayerRepository playerRepository;

  public PlayerServiceImpl(
      PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  @Override
  public PlayerEntity findByUsername (String username) {

    return playerRepository.findByUsername(username);
  }

  @Override
  public PlayerEntity loginUser(PlayerEntity playerInput) {
    PlayerEntity player = findByUsername(playerInput.getUsername());
    if(playerInput.getPassword().equals(player.getPassword())){
      return player;
    } else return null;
  }
}
