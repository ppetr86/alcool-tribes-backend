package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.models.UserEntity;
import com.greenfoxacademy.springwebapp.models.dtos.UserDTO;
import com.greenfoxacademy.springwebapp.models.dtos.UserErrorDTO;
import com.greenfoxacademy.springwebapp.repositories.RegistrationRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {
  private RegistrationRepo registrationRepo;

  public RegistrationServiceImpl(RegistrationRepo registrationRepo) {
    this.registrationRepo = registrationRepo;
  }

  @Override
  public ResponseEntity<?> createUser(UserDTO userDTO) {
    if (userDTO.getUsername() == null) {
      UserErrorDTO error = new UserErrorDTO("Username is required");
      return ResponseEntity.status(HttpStatus.valueOf(400)).body(error);
    } else if (registrationRepo.findByUsername(userDTO.getUsername()) != null) {
      UserErrorDTO error = new UserErrorDTO("Username is already taken.");
      return ResponseEntity.status(HttpStatus.valueOf(409)).body(error);
    } else if (userDTO.getPassword() == null) {
      UserErrorDTO error = new UserErrorDTO("Password is required.");
      return ResponseEntity.status(HttpStatus.valueOf(400)).body(error);
    } else if (userDTO.getPassword().length() < 8) {
      UserErrorDTO error = new UserErrorDTO("Password must be 8 characters.");
      return ResponseEntity.status(HttpStatus.valueOf(406)).body(error);
    } else {
      UserEntity userEntity = new UserEntity();
      userEntity.setUsername(userDTO.getUsername());
      userEntity.setPassword(userDTO.getPassword());
      userEntity.setEmail(userDTO.getEmail());
      if (userDTO.getKingdomName() != null) {
        userEntity.setKingdomName(userDTO.getKingdomName());
      } else {
        userEntity.setKingdomName(userDTO.getUsername()+"'s kingdom");
      }
      registrationRepo.save(userEntity);
      return ResponseEntity.ok().body(userEntity);
    }
  }
}
