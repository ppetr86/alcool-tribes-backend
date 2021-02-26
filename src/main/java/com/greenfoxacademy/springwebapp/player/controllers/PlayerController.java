package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidTokenException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.UsernameIsTakenException;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegisterRequestDTO;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(PlayerController.URI)
public class PlayerController {
  private final PlayerService playerService;

  public static final String URI = "/register";

  @GetMapping("/verify")
  @ResponseBody
  public String verifyUser(@RequestParam(required = false) String token) {

    if (token.isEmpty()) {
      log.info(String.format("Verification token was incorrect. Used token: %s", token));
      return "empty token";
    }
    try {
      playerService.verifyUser(token);
    } catch (InvalidTokenException e) {
      log.info("Verification token is invalid. Used token: {}", token);
      return "invalid token";
    }
    log.info(String.format("Token: %s was verified", token));
    return "verified";
  }


  @PostMapping
  public ResponseEntity<?> registerUser(@RequestBody @Valid PlayerRegisterRequestDTO request)
          throws UsernameIsTakenException {
    //if you want to use the text from validation annotation then you can not
    // combine it with Binding result
    // if you dont combine, default messages will be displayed and exception thrown automatically
    PlayerEntity newRegistration = playerService.registerNewPlayer(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(playerService.playerToResponseDTO(newRegistration));
  }
}