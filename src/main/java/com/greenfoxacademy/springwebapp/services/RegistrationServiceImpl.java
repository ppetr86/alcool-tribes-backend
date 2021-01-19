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
  public ResponseEntity<?> createUser(UserDTO userDTO) {
    if (userDTO.getUsername() == null && userDTO.getPassword() == null){
      UserErrorDTO error = new UserErrorDTO("Username and password are required.");
      return ResponseEntity.status(HttpStatus.valueOf(400)).body(error);
    } else if (userDTO.getUsername() == null) {
      UserErrorDTO error = new UserErrorDTO("Username is required");
      return ResponseEntity.status(HttpStatus.valueOf(400)).body(error);
    } else if (userDTO.getPassword() == null) {
      UserErrorDTO error = new UserErrorDTO("Password is required.");
      return ResponseEntity.status(HttpStatus.valueOf(400)).body(error);
    } else if (registrationRepo.findByUsername(userDTO.getUsername()) != null) {
      UserErrorDTO error = new UserErrorDTO("Username is already taken.");
      return ResponseEntity.status(HttpStatus.valueOf(409)).body(error);
    } else if (userDTO.getPassword().length() < 8) {
      UserErrorDTO error = new UserErrorDTO("Password must be 8 characters.");
      return ResponseEntity.status(HttpStatus.valueOf(406)).body(error);
    } else {
      UserEntity userEntity = new UserEntity();
      userEntity.setUsername(userDTO.getUsername());
      userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
      userEntity.setEmail(userDTO.getEmail());
      KingdomEntity kingdomEntity = new KingdomEntity();
      if (userDTO.getKingdomname() != null) {
        kingdomEntity.setKingdomName(userDTO.getKingdomname());
      } else {
        kingdomEntity.setKingdomName(userDTO.getUsername()+"'s kingdom");
      }
      userEntity.setKingdomEntity(kingdomEntity);
      registrationRepo.save(userEntity);
      return ResponseEntity.status(HttpStatus.valueOf(201)).body(userEntity);
    }
  }
}
