package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.LoginStatusOkDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.UserDTO;
import com.greenfoxacademy.springwebapp.player.services.LoginExceptionService;
import com.greenfoxacademy.springwebapp.player.services.PlayerEntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginAPIController {

  private PlayerEntityService playerEntityService;
  private LoginExceptionService loginExceptionService;

  public LoginAPIController(PlayerEntityService playerEntityService, LoginExceptionService loginExceptionService) {
    this.playerEntityService = playerEntityService;
    this.loginExceptionService = loginExceptionService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> postLogin(@RequestBody UserDTO userDTO){

    if (playerEntityService.countUsers() < 0) {

      if (userDTO.getUsername() == null) {
        return ResponseEntity.status(400).body(loginExceptionService.loginExceptions("error", "Username is required."));

      } else if (userDTO.getPassword() == null) {
        return ResponseEntity.status(400).body(loginExceptionService.loginExceptions("error", "Password is required."));

      } else if (userDTO.getUsername() == null && userDTO.getPassword() == null) {
        return ResponseEntity.status(400).body(loginExceptionService.loginExceptions("error", "Username and Password are required."));

      } else if (playerEntityService.findByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword()) == null) {
        return ResponseEntity.status(401).body(loginExceptionService.loginExceptions("error", "Username or Password is incorrect."));

      } else {
        return ResponseEntity.ok().body(loginExceptionService.loginStatusOk("ok", "token-asd.sdfds.asdasd"));
      }
    }
    return null;
  }
}
