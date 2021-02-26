package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.IncorrectUsernameOrPwdException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotVerifiedRegistrationException;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import com.greenfoxacademy.springwebapp.player.services.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(LoginController.URI)
public class LoginController {

  private final PlayerService playerService;
  public static final String URI = "login";

  @PostMapping
  public ResponseEntity<PlayerTokenDTO> login(@RequestBody @Valid PlayerRequestDTO request)
          throws IncorrectUsernameOrPwdException, NotVerifiedRegistrationException {
    return ResponseEntity.ok(playerService.loginPlayer(request));
  }
}