package com.greenfoxacademy.springwebapp.email.services;

import com.greenfoxacademy.springwebapp.email.models.RegistrationTokenEntity;

public interface RegistrationTokenService {

    RegistrationTokenEntity createSecureToken();
    void saveSecureToken(final RegistrationTokenEntity token);
    RegistrationTokenEntity findByToken(final String token);
    void removeToken(final RegistrationTokenEntity token);
    void removeTokenByToken(final String token);
}
