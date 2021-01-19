package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.models.UserEntity;
import com.greenfoxacademy.springwebapp.models.dtos.UserDTO;
import com.greenfoxacademy.springwebapp.models.dtos.UserErrorDTO;
import com.greenfoxacademy.springwebapp.repositories.RegistrationRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public UserEntity saveUser(UserDTO userDTO) {
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(userDTO.getUsername());
    userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    userEntity.setEmail(userDTO.getEmail());
    KingdomEntity kingdomEntity = new KingdomEntity();
    if (userDTO.getKingdomname() != null) {
      kingdomEntity.setKingdomName(userDTO.getKingdomname());
    } else {
      kingdomEntity.setKingdomName(userDTO.getUsername() + "'s kingdom");
    }
    userEntity.setKingdomEntity(kingdomEntity);
    registrationRepo.save(userEntity);
    return userEntity;
  }

  @Override
  public UserEntity findByUsername(String username) {
    return registrationRepo.findByUsername(username);
  }
}
