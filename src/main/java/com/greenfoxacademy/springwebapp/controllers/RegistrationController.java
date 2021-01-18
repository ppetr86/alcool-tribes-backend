package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.models.dtos.UserDTO;
import com.greenfoxacademy.springwebapp.services.RegistrationService;
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
  public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO){
    ResponseEntity<?> response = registrationService.createUser(userDTO);
    return ResponseEntity.status(response.getStatusCode()).body(response);
  }
}
