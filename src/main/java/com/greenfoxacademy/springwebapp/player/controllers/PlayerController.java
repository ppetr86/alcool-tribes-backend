package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.configuration.email.EmailService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidTokenException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegisterRequestDTO;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
  private MessageSource messageSource;


  @GetMapping("/register/verify")
  @ResponseBody
  public String verifyUser(@RequestParam(required = false) String token, RedirectAttributes redirAttr) {
    if (token.isEmpty()) {
      //redirAttr.addFlashAttribute("tokenError", messageSource.getMessage("user.registration.verification.missing.token", null, LocaleContextHolder.getLocale()));
      return "empty token";
    }
    try {
      playerService.verifyUser(token);
    } catch (InvalidTokenException e) {
      //redirAttr.addFlashAttribute("tokenError", messageSource.getMessage("user.registration.verification.invalid.token", null, LocaleContextHolder.getLocale()));
      return "invalid token";
    }
    //redirAttr.addFlashAttribute("verifiedAccountMsg", messageSource.getMessage("user.registration.verification.success", null, LocaleContextHolder.getLocale()));
    return "verified";
  }


  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody @Valid PlayerRegisterRequestDTO request,
                                        BindingResult bindingResult)
          throws MessagingException, IOException {

    PlayerEntity newRegistration = playerService.registerNewPlayer(request, bindingResult);
    return ResponseEntity.ok(playerService.playerToResponseDTO(newRegistration));
  }
}