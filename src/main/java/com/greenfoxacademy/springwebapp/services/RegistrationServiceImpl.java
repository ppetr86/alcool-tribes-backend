package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.models.dtos.PlayerDTO;
import com.greenfoxacademy.springwebapp.repositories.RegistrationRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {
  private RegistrationRepo registrationRepo;
  private PasswordEncoder passwordEncoder;

  public RegistrationServiceImpl(
      RegistrationRepo registrationRepo,
      PasswordEncoder passwordEncoder) {
    this.registrationRepo = registrationRepo;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public PlayerEntity saveUser(PlayerDTO playerDTO) {
    PlayerEntity playerEntity = new PlayerEntity();
    playerEntity.setUsername(playerDTO.getUsername());
    playerEntity.setPassword(passwordEncoder.encode(playerDTO.getPassword()));
    playerEntity.setEmail(playerDTO.getEmail());
    KingdomEntity kingdomEntity = new KingdomEntity();
    if (playerDTO.getKingdomname() != null) {
      kingdomEntity.setKingdomName(playerDTO.getKingdomname());
    } else {
      kingdomEntity.setKingdomName(playerDTO.getUsername() + "'s kingdom");
    }
    playerEntity.setKingdomEntity(kingdomEntity);
    registrationRepo.save(playerEntity);
    return playerEntity;
  }

  @Override
  public PlayerEntity findByUsername(String username) {
    return registrationRepo.findByUsername(username);
  }
}
