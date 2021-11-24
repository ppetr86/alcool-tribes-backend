package com.greenfoxacademy.springwebapp.email.services;

import com.greenfoxacademy.springwebapp.email.models.RegistrationTokenEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;

public interface RegistrationTokenService {

    RegistrationTokenEntity createSecureToken(PlayerEntity player);


    RegistrationTokenEntity findByToken(final String token);


    void removeToken(final RegistrationTokenEntity token);


    void removeTokenByToken(final String token);


    void saveSecureToken(final RegistrationTokenEntity token);
}
