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
      return ResponseEntity.ok().body(loginExceptionService.generateTokenToLoggedInPlayer(userDTO));
    } else {
      String error = errorList.get(0).getCode();
      return ResponseEntity.status(400).body(new ErrorMessageDTO("error", errorList.get(0).getDefaultMessage()));
    }
  }
}
