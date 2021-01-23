package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.UserDTO;
import com.greenfoxacademy.springwebapp.player.services.LoginExceptionService;
import com.greenfoxacademy.springwebapp.player.services.PlayerEntityService;
import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
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
public class LoginAPIController {

  private PlayerEntityService playerEntityService;
  private LoginExceptionService loginExceptionService;

  public LoginAPIController(PlayerEntityService playerEntityService, LoginExceptionService loginExceptionService) {
    this.playerEntityService = playerEntityService;
    this.loginExceptionService = loginExceptionService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> postLogin(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {

    List<ObjectError> errorList = bindingResult.getAllErrors();

    if (errorList.isEmpty()) {
      if (playerEntityService.countPlayers() > 0 &&
        playerEntityService.findByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword()) != null) {
        return ResponseEntity.ok().body(loginExceptionService.generateTokenToLoggedInPlayer(userDTO));
      } else if (playerEntityService.findByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword()) == null) {
        return ResponseEntity.status(401).body(new ErrorMessageDTO("error", "Username or password is incorrect."));
      } else {
        return ResponseEntity.status(404).body(new ErrorMessageDTO("error", "No player in the database."));
      }
    } else {
      String error = errorList.get(0).getCode(); //check in debug, what is the main error message if we have more
      if (userDTO.getPassword() == null && userDTO.getUsername() == null) {
        return ResponseEntity.status(400).body(new ErrorMessageDTO("error", "Username and password are required."));
      }
      return ResponseEntity.status(400).body(new ErrorMessageDTO("error", errorList.get(0).getDefaultMessage()));
    }
  }
}
