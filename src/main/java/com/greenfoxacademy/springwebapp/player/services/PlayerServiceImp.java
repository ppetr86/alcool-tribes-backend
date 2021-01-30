package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.repositories.PlayerRepository;
import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImp implements PlayerService {

  private PlayerRepository playerRepository;
  private PasswordEncoder passwordEncoder;
  private JwtProvider jwtProvider;

  public PlayerServiceImp(PlayerRepository playerRepository,
                          PasswordEncoder passwordEncoder,
                          JwtProvider jwtProvider) {
    this.playerRepository = playerRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtProvider = jwtProvider;
  }

  @Override
  public PlayerEntity findByUsername(String username) {
    return playerRepository.findByUsername(username);
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
