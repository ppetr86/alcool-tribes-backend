package com.greenfoxacademy.springwebapp.email.services;

import com.greenfoxacademy.springwebapp.email.models.RegistrationTokenEntity;
import com.greenfoxacademy.springwebapp.email.repository.RegistrationTokenRepository;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationTokenServiceImpl implements RegistrationTokenService {

  private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);
  private static final Charset US_ASCII = Charset.forName("US-ASCII");
  private final RegistrationTokenRepository secureTokenRepository;
  private final Environment env;

  @Override
  public RegistrationTokenEntity createSecureToken(PlayerEntity player) {
    String tokenValue = new String(Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey()), US_ASCII);
    RegistrationTokenEntity secureToken = new RegistrationTokenEntity();
    secureToken.setToken(tokenValue);
    secureToken.setIsExpired(false);
    secureToken.setExpireAt(LocalDateTime.now().plusSeconds(getTokenValidityInSeconds()));
    secureToken.setPlayer(player);
    this.saveSecureToken(secureToken);
    return secureToken;
  }

  @Override
  public void saveSecureToken(RegistrationTokenEntity token) {
    secureTokenRepository.save(token);
  }

  @Override
  public RegistrationTokenEntity findByToken(String token) {
    return secureTokenRepository.findByToken(token);
  }

  @Override
  public void removeToken(RegistrationTokenEntity token) {
    secureTokenRepository.delete(token);
  }

  @Override
  public void removeTokenByToken(String token) {
    secureTokenRepository.removeByToken(token);
  }

  public int getTokenValidityInSeconds() {
    return Integer.parseInt(env.getProperty("jdj.secure.token.validity"));
  }
}