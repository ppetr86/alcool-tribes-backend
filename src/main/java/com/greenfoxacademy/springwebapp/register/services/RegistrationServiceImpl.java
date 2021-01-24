package com.greenfoxacademy.springwebapp.register.services;

import com.greenfoxacademy.springwebapp.register.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.register.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.register.models.dtos.PlayerDTO;
import com.greenfoxacademy.springwebapp.register.repositories.RegistrationRepo;
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
  public PlayerEntity savePlayer(PlayerDTO dto) {
    KingdomEntity kingdom = new KingdomEntity();
    if (dto.getKingdomname() != null) {
      kingdom.setKingdomName(dto.getKingdomname());
    } else {
      kingdom.setKingdomName(dto.getUsername() + "'s kingdom");
    }
    PlayerEntity playerEntity =
        new PlayerEntity(dto.getUsername(), passwordEncoder.encode(dto.getPassword()), dto.getEmail(), kingdom);
    registrationRepo.save(playerEntity);
    return playerEntity;
  }

  @Override
  public PlayerEntity findByUsername(String username) {
    return registrationRepo.findByUsername(username);
  }
}
