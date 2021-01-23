package com.greenfoxacademy.springwebapp.register.repositories;

import com.greenfoxacademy.springwebapp.register.models.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepo extends JpaRepository<PlayerEntity, Long> {

  PlayerEntity findByUsername(String username);

}
