package com.greenfoxacademy.springwebapp.email.services;

import com.greenfoxacademy.springwebapp.email.models.SecureTokenEntity;

public interface SecureTokenService {

    SecureTokenEntity createSecureToken();
    void saveSecureToken(final SecureTokenEntity token);
    SecureTokenEntity findByToken(final String token);
    void removeToken(final SecureTokenEntity token);
    void removeTokenByToken(final String token);
}
