package com.greenfoxacademy.springwebapp.configuration.email.repository;


import com.greenfoxacademy.springwebapp.configuration.email.SecureToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecureTokenRepository extends JpaRepository<SecureToken, Long> {

  SecureToken findByToken(final String token);

  Long removeByToken(String token);
}
