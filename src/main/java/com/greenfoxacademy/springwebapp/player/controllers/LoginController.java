package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class LoginController {
  PlayerService playerService;
  JwtProvider jwtProvider;

  public LoginController(PlayerService playerService,
                         JwtProvider jwtProvider) {
    this.playerService = playerService;
    this.jwtProvider = jwtProvider;
  }

  @GetMapping("/login")
  public ResponseEntity<?> postLogin(@RequestBody PlayerEntity playerInput){

    PlayerEntity loggedPlayer = playerService.loginUser(playerInput);
    if (loggedPlayer != null) {
      String token = jwtProvider.generateToken(loggedPlayer.getUsername());
      PlayerTokenDTO tokenDTO = new PlayerTokenDTO(token);
      return ResponseEntity.ok().body(tokenDTO);
    } else {
      return ResponseEntity.status(HttpStatus.valueOf(401)).build();
    }
  }

  @GetMapping("/test")
  public ResponseEntity<?> testSecurity(@RequestBody PlayerEntity playerInput){

    PlayerEntity loggedPlayer = playerService.loginUser(playerInput);
    if (loggedPlayer != null) {
      String token = jwtProvider.generateToken(loggedPlayer.getUsername());
      PlayerTokenDTO tokenDTO = new PlayerTokenDTO(token);
      return ResponseEntity.ok().body(tokenDTO);
    } else {
      return ResponseEntity.status(HttpStatus.valueOf(401)).build();
    }
  }
}
