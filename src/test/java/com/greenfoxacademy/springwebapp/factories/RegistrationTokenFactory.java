package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.email.models.RegistrationTokenEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;

import java.time.LocalDateTime;

public class RegistrationTokenFactory {

  public static RegistrationTokenEntity createToken(PlayerEntity player) {
    RegistrationTokenEntity secureToken = new RegistrationTokenEntity();
    secureToken.setToken("123");
    secureToken.setIsExpired(false);
    secureToken.setPlayer(player);
    secureToken.setExpireAt(LocalDateTime.now().plusDays(1));
    return secureToken;
  }
}