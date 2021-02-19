package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerResponseDTO;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
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
public class PlayerController {
  private final PlayerService playerService;

  public PlayerController(PlayerService playerService) {
    this.playerService = playerService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody @Valid PlayerRegistrationRequestDTO playerRegistrationRequestDTO,
                                        BindingResult bindingResult) {
    List<ObjectError> errorList = bindingResult.getAllErrors();

    if (errorList.isEmpty()) {
      if (playerService.findByUsername(playerRegistrationRequestDTO.getUsername()) != null) {
        return ResponseEntity.status(HttpStatus.valueOf(409)).body(new ErrorDTO("Username is already taken."));
      }
      PlayerResponseDTO responsePlayerEntity = playerService.saveNewPlayer(playerRegistrationRequestDTO);
      return ResponseEntity.status(HttpStatus.valueOf(201)).body(responsePlayerEntity);
    } else if (playerRegistrationRequestDTO.getUsername() == null &&
            playerRegistrationRequestDTO.getPassword() == null) {
      return ResponseEntity.status(HttpStatus.valueOf(400))
              .body(new ErrorDTO("Username and password are required."));
    } else if (playerRegistrationRequestDTO.getPassword() == null ||
            playerRegistrationRequestDTO.getPassword().length() < 8) {
      return ResponseEntity.status(HttpStatus.valueOf(406))
              .body(new ErrorDTO(errorList.get(0).getDefaultMessage()));
    }

    return ResponseEntity.status(HttpStatus.valueOf(400))
            .body(new ErrorDTO(errorList.get(0).getDefaultMessage()));
  }
}