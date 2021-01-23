package com.greenfoxacademy.springwebapp.register.controllers;

import com.greenfoxacademy.springwebapp.register.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.register.models.dtos.RegisterResponseDTO;
import com.greenfoxacademy.springwebapp.register.models.dtos.PlayerDTO;
import com.greenfoxacademy.springwebapp.register.models.dtos.RegisterErrorDTO;
import com.greenfoxacademy.springwebapp.register.services.RegistrationService;
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
  public ResponseEntity<?> registerUser(@RequestBody PlayerDTO playerDTO) {
    if (playerDTO.getUsername() == null && playerDTO.getPassword() == null) {
      RegisterErrorDTO error = new RegisterErrorDTO("Username and password are required.");
      return ResponseEntity.status(HttpStatus.valueOf(400)).body(error);
    } else if (playerDTO.getUsername() == null) {
      RegisterErrorDTO error = new RegisterErrorDTO("Username is required.");
      return ResponseEntity.status(HttpStatus.valueOf(400)).body(error);
    } else if (playerDTO.getPassword() == null) {
      RegisterErrorDTO error = new RegisterErrorDTO("Password is required.");
      return ResponseEntity.status(HttpStatus.valueOf(400)).body(error);
    } else if (registrationService.findByUsername(playerDTO.getUsername()) != null) {
      RegisterErrorDTO error = new RegisterErrorDTO("Username is already taken.");
      return ResponseEntity.status(HttpStatus.valueOf(409)).body(error);
    } else if (playerDTO.getPassword().length() < 8) {
      RegisterErrorDTO error = new RegisterErrorDTO("Password must be 8 characters.");
      return ResponseEntity.status(HttpStatus.valueOf(406)).body(error);
    } else {
      PlayerEntity playerEntity = registrationService.saveUser(playerDTO);
      RegisterResponseDTO responseDTO = new RegisterResponseDTO();
      responseDTO.setId(playerEntity.getId());
      responseDTO.setUsername(playerEntity.getUsername());
      responseDTO.setEmail(playerEntity.getEmail());
      responseDTO.setKingdomId(playerEntity.getKingdomEntity().getId());
      responseDTO.setAvatar(playerEntity.getAvatar());
      responseDTO.setPoints(playerEntity.getPoints());
      return ResponseEntity.status(HttpStatus.valueOf(201)).body(responseDTO);

    }
  }
}
