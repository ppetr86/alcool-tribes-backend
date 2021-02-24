package com.greenfoxacademy.springwebapp.email.services;



import com.greenfoxacademy.springwebapp.email.models.SecureTokenEntity;
import com.greenfoxacademy.springwebapp.email.repository.SecureTokenRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

@Service
public class SecureTokenServiceImpl implements SecureTokenService {

  private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);
  private static final Charset US_ASCII = Charset.forName("US-ASCII");

  @Value("${jdj.secure.token.validity}")
  private int tokenValidityInSeconds;

  @Autowired
  SecureTokenRepository secureTokenRepository;

  @Override
  public SecureTokenEntity createSecureToken(){
    String tokenValue = new String(Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey()), US_ASCII); // this is a sample, you can adapt as per your security need
    SecureTokenEntity secureToken = new SecureTokenEntity();
    secureToken.setToken(tokenValue);
    secureToken.setExpireAt(LocalDateTime.now().plusSeconds(getTokenValidityInSeconds()));
    this.saveSecureToken(secureToken);
    return secureToken;
  }

  @Override
  public void saveSecureToken(SecureTokenEntity token) {
    secureTokenRepository.save(token);
  }

  @Override
  public SecureTokenEntity findByToken(String token) {
    return secureTokenRepository.findByToken(token);
  }

  @Override
  public void removeToken(SecureTokenEntity token) {
    secureTokenRepository.delete(token);
  }

  @Override
  public void removeTokenByToken(String token) {
    secureTokenRepository.removeByToken(token);
  }

  public int getTokenValidityInSeconds() {
    return tokenValidityInSeconds;
  }
}
