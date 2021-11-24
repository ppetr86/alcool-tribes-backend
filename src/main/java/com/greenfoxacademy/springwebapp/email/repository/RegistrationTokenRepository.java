package com.greenfoxacademy.springwebapp.email.repository;

import com.greenfoxacademy.springwebapp.email.models.RegistrationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationTokenRepository extends JpaRepository<RegistrationTokenEntity, Long> {

    RegistrationTokenEntity findByToken(final String token);


    Long removeByToken(String token);
}
