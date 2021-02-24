package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.email.services.EmailService;
import com.greenfoxacademy.springwebapp.email.services.SecureTokenService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidTokenException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.UsernameIsTakenException;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegisterRequestDTO;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Slf4j
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

  private SecureTokenService tokenService;


  @GetMapping("/register/verify")
  @ResponseBody
  public String verifyUser(@RequestParam(required = false) String token, RedirectAttributes redirAttr) {

    if (token.isEmpty()) {
      //redirAttr.addFlashAttribute("tokenError", messageSource.getMessage("user.registration.verification.missing.token", null, LocaleContextHolder.getLocale()));
      log.info(String.format("Verification token was incorrect. Used token: %s", token));
      return "empty token";
    }
    try {
      playerService.verifyUser(token);
    } catch (InvalidTokenException e) {
      //redirAttr.addFlashAttribute("tokenError", messageSource.getMessage("user.registration.verification.invalid.token", null, LocaleContextHolder.getLocale()));
      log.info("Verification token is invalid. Used token: {}", token);
      return "invalid token";
    }
    //redirAttr.addFlashAttribute("verifiedAccountMsg", messageSource.getMessage("user.registration.verification.success", null, LocaleContextHolder.getLocale()));
    String registeringPlayer = tokenService.findByToken(token).getPlayer().getUsername();
    log.info(String.format("Token: %s was verified", token));
    return "verified";
  }


  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody @Valid PlayerRegisterRequestDTO request)
          throws UsernameIsTakenException {
    //if you want to use the text from validation annotation then you can not
    // combine it with Binding result
    // if you dont combine, default messages will be displayed and exception thrown automatically
    PlayerEntity newRegistration = playerService.registerNewPlayer(request);
    return ResponseEntity.ok(playerService.playerToResponseDTO(newRegistration));
  }
}