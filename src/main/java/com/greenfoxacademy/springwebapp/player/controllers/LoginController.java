package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
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

  private final PlayerService playerService;
  private final TokenService tokenService;

  public LoginController(PlayerService playerService, TokenService tokenService) {
    this.playerService = playerService;
    this.tokenService = tokenService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Valid PlayerRequestDTO request,
                                 BindingResult bindingResult) {

    List<ObjectError> errorList = bindingResult.getAllErrors();

    if (errorList.isEmpty()) {
      if (playerService.findByUsernameAndPassword(request.getUsername(), request.getPassword()) != null) {
        // if player isVerfified = false - dont allow login
        // do what when not verified?? Login will not succeed
        if (!playerService.findIsVerified(request.getUsername()))
          return ResponseEntity.status(401).body(new ErrorDTO("Not verified username"));

        PlayerTokenDTO playerTokenDTO = tokenService.generateTokenToLoggedInPlayer(request);
        return ResponseEntity.ok().body(playerTokenDTO);
      } else {
        System.out.println("test2");
        return ResponseEntity.status(401).body(new ErrorDTO("Username or password is incorrect."));
      }
    } else {
      String error = errorList.get(0).getCode(); //check in debug, what is the main error message if we have more
      if (request.getPassword() == null && request.getUsername() == null) {
        return ResponseEntity.status(400).body(new ErrorDTO("Username and password are required."));
      }
      return ResponseEntity.status(400).body(new ErrorDTO(errorList.get(0).getDefaultMessage()));
    }
  }
}