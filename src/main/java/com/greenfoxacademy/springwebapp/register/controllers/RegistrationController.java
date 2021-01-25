package com.greenfoxacademy.springwebapp.register.controllers;

import com.greenfoxacademy.springwebapp.register.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.register.models.dtos.PlayerRegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.register.models.dtos.RegisterResponseDTO;
import com.greenfoxacademy.springwebapp.register.models.dtos.ErrorDTO;
import com.greenfoxacademy.springwebapp.register.services.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RegistrationController {
  private RegistrationService registrationService;

  public RegistrationController(RegistrationService registrationService) {
    this.registrationService = registrationService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody @Valid PlayerRegistrationRequestDTO playerRegistrationRequestDTO, BindingResult bindingResult) {
    List<ObjectError> errorList = bindingResult.getAllErrors();

    if (errorList.isEmpty()) {
      if (registrationService.findByUsername(playerRegistrationRequestDTO.getUsername()) != null) {
        return ResponseEntity.status(HttpStatus.valueOf(409)).body(new ErrorDTO("Username is already taken."));
      }
      PlayerEntity playerEntity = registrationService.savePlayer(playerRegistrationRequestDTO);
      RegisterResponseDTO responseDTO = new RegisterResponseDTO();
      responseDTO.setId(playerEntity.getId());
      responseDTO.setUsername(playerEntity.getUsername());
      responseDTO.setEmail(playerEntity.getEmail());
      responseDTO.setKingdomId(playerEntity.getKingdomEntity().getId());
      responseDTO.setAvatar(playerEntity.getAvatar());
      responseDTO.setPoints(playerEntity.getPoints());
      return ResponseEntity.status(HttpStatus.valueOf(201)).body(responseDTO);
    } else if (playerRegistrationRequestDTO.getUsername() == null && playerRegistrationRequestDTO.getPassword() == null) {
      return ResponseEntity.status(HttpStatus.valueOf(400))
          .body(new ErrorDTO("Username and password are required."));
    } else if (playerRegistrationRequestDTO.getPassword().length() < 8){
      return ResponseEntity.status(HttpStatus.valueOf(406))
          .body(new ErrorDTO(errorList.get(0).getDefaultMessage()));
    }

    return ResponseEntity.status(HttpStatus.valueOf(400))
        .body(new ErrorDTO(errorList.get(0).getDefaultMessage()));
  }
}

