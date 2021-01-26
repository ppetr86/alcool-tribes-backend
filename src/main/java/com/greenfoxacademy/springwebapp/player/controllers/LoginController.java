package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.player.models.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerDTO;
import com.greenfoxacademy.springwebapp.player.services.PlayerEntityService;
import com.greenfoxacademy.springwebapp.player.services.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class LoginController {

  private PlayerEntityService playerEntityService;
  private TokenService tokenService;

  public LoginController(PlayerEntityService playerEntityService, TokenService tokenService) {
    this.playerEntityService = playerEntityService;
    this.tokenService = tokenService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> postLogin(@RequestBody @Valid PlayerDTO playerDTO, BindingResult bindingResult) {

    List<ObjectError> errorList = bindingResult.getAllErrors();

    if (errorList.isEmpty()) {
      if (playerEntityService.findByUsernameAndPassword(playerDTO.getUsername(), playerDTO.getPassword()) != null) {
        PlayerTokenDTO playerTokenDTO = tokenService.generateTokenToLoggedInPlayer(playerDTO);
        return ResponseEntity.ok().body(playerTokenDTO);
      } else {
        return ResponseEntity.status(401).body(new ErrorMessageDTO("Username or password is incorrect."));
      }
    } else {
      String error = errorList.get(0).getCode(); //check in debug, what is the main error message if we have more
      if (playerDTO.getPassword() == null && playerDTO.getUsername() == null) {
        return ResponseEntity.status(400).body(new ErrorMessageDTO("Username and password are required."));
      }
      return ResponseEntity.status(400).body(new ErrorMessageDTO(errorList.get(0).getDefaultMessage()));
    }
  }
}
