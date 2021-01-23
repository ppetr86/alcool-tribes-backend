package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.repositories.PlayerEntityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PlayerEntityServiceImp implements PlayerEntityService {

  private PlayerEntityRepository playerEntityRepository;
  private PasswordEncoder passwordEncoder;

  public PlayerEntityServiceImp(PlayerEntityRepository playerEntityRepository, PasswordEncoder passwordEncoder) {
    this.playerEntityRepository = playerEntityRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public long countPlayers() {
    return playerEntityRepository.count();
  }

  @Override
  public PlayerEntity findByUsername(String username) {
    return playerEntityRepository.findByUsername(username);
  }

  @Override
  public PlayerEntity findByUsernameAndPassword(String username, String password) {
    PlayerEntity playerEntity = findByUsername(username);

    if (playerEntity != null) {
      if (passwordEncoder.matches(password, playerEntity.getPassword())) {
        return playerEntity;
      }
    }
    return null;
  }
}
