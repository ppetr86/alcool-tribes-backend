package com.greenfoxacademy.springwebapp.configuration.email.repository;


import com.greenfoxacademy.springwebapp.configuration.email.models.SecureTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecureTokenRepository extends JpaRepository<SecureTokenEntity, Long> {

  SecureTokenEntity findByToken(final String token);

  Long removeByToken(String token);
}
