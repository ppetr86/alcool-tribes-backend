package com.greenfoxacademy.springwebapp.repositories;

import com.greenfoxacademy.springwebapp.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepo extends JpaRepository<UserEntity, Long> {

  UserEntity findByUsername(String username);

}
