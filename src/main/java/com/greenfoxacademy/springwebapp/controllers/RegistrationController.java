package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.models.UserEntity;
import com.greenfoxacademy.springwebapp.models.dtos.RegisterResponseDTO;
import com.greenfoxacademy.springwebapp.models.dtos.UserDTO;
import com.greenfoxacademy.springwebapp.models.dtos.UserErrorDTO;
import com.greenfoxacademy.springwebapp.services.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
  private RegistrationService registrationService;

  public RegistrationController(RegistrationService registrationService) {
    this.registrationService = registrationService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
    if (userDTO.getUsername() == null && userDTO.getPassword() == null) {
      UserErrorDTO error = new UserErrorDTO("Username and password are required.");
      return ResponseEntity.status(HttpStatus.valueOf(400)).body(error);
    } else if (userDTO.getUsername() == null) {
      UserErrorDTO error = new UserErrorDTO("Username is required");
      return ResponseEntity.status(HttpStatus.valueOf(400)).body(error);
    } else if (userDTO.getPassword() == null) {
      UserErrorDTO error = new UserErrorDTO("Password is required.");
      return ResponseEntity.status(HttpStatus.valueOf(400)).body(error);
    } else if (registrationService.findByUsername(userDTO.getUsername()) != null) {
      UserErrorDTO error = new UserErrorDTO("Username is already taken.");
      return ResponseEntity.status(HttpStatus.valueOf(409)).body(error);
    } else if (userDTO.getPassword().length() < 8) {
      UserErrorDTO error = new UserErrorDTO("Password must be 8 characters.");
      return ResponseEntity.status(HttpStatus.valueOf(406)).body(error);
    } else {
      UserEntity userEntity = registrationService.saveUser(userDTO);
      RegisterResponseDTO responseDTO = new RegisterResponseDTO();
      responseDTO.setId(userEntity.getId());
      responseDTO.setUsername(userEntity.getUsername());
      responseDTO.setEmail(userEntity.getEmail());
      responseDTO.setKingdomId(userEntity.getKingdomEntity().getId());
      responseDTO.setAvatar(userEntity.getAvatar());
      responseDTO.setPoints(userEntity.getPoints());
      return ResponseEntity.status(HttpStatus.valueOf(201)).body(responseDTO);

    }
  }
}
