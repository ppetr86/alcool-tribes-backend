package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidTokenException;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class RegistrationController {

  private PlayerService playerService;
  private MessageSource messageSource;


  @GetMapping("/register/verify")
  public String verifyCustomer(@RequestParam(required = false) String token, final Model model, RedirectAttributes redirAttr) {
    if (token.isEmpty()) {
      redirAttr.addFlashAttribute("tokenError", messageSource.getMessage("user.registration.verification.missing.token", null, LocaleContextHolder.getLocale()));
      return "empty token";
    }
    try {
      playerService.verifyUser(token);
    } catch (InvalidTokenException e) {
      redirAttr.addFlashAttribute("tokenError", messageSource.getMessage("user.registration.verification.invalid.token", null, LocaleContextHolder.getLocale()));
      return "invalid token";
    }

    redirAttr.addFlashAttribute("verifiedAccountMsg", messageSource.getMessage("user.registration.verification.success", null, LocaleContextHolder.getLocale()));
    return "verified";
  }

}
