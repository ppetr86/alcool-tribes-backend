package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
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

  public static final String URI = "login";
  private final PlayerService playerService;

  @PostMapping
  public ResponseEntity<PlayerTokenDTO> login(@RequestBody @Valid PlayerRequestDTO request)
      throws RuntimeException {
    return ResponseEntity.ok(playerService.loginPlayer(request));
  }
}