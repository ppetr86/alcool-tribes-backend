package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.models.UserEntity;
import com.greenfoxacademy.springwebapp.models.dtos.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface RegistrationService {

  UserEntity saveUser(UserDTO userDTO);

  UserEntity findByUsername(String username);
}
