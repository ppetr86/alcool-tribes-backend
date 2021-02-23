package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.configuration.email.EmailService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegisterRequestDTO;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class PlayerController {
  private final PlayerService playerService;
  private final EmailService emailService;
  private final KingdomService kingdomService;
  private ApplicationEventPublisher eventPublisher;
  private MessageSource messages;
  private Environment env;


  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody @Valid PlayerRegisterRequestDTO request,
                                        BindingResult bindingResult, HttpServletRequest servletRequest)
          throws MessagingException, IOException {


    List<ObjectError> errorList = bindingResult.getAllErrors();

    if (errorList.isEmpty()) {
      if (playerService.findByUsername(request.getUsername()) != null) {
        return ResponseEntity.status(HttpStatus.valueOf(409)).body(new ErrorDTO("Username is already taken."));
      }

      PlayerEntity savedPlayer = playerService.saveNewPlayer(request);

      if (!request.getEmail().isEmpty()) {
        //emailService.sendHtmlEmail(request.getEmail(), request.getUsername(), kingdomService.kingdomNameByPlayerID(response.getId()));
        //emailService.sendTextEmail(request.getEmail(), request.getUsername(), kingdomService.kingdomNameByPlayerID(response.getId()));
        playerService.sendRegistrationConfirmationEmail(savedPlayer);
      }
      return ResponseEntity.status(HttpStatus.valueOf(201)).body(playerService.playerToResponseDTO(savedPlayer));

    } else if (request.getUsername() == null &&
            request.getPassword() == null) {
      return ResponseEntity.status(HttpStatus.valueOf(400))
              .body(new ErrorDTO("Username and password are required."));
    } else if (request.getPassword() == null ||
            request.getPassword().length() < 8) {
      return ResponseEntity.status(HttpStatus.valueOf(406))
              .body(new ErrorDTO(errorList.get(0).getDefaultMessage()));
    }

    return ResponseEntity.status(HttpStatus.valueOf(400))
            .body(new ErrorDTO(errorList.get(0).getDefaultMessage()));
  }
}