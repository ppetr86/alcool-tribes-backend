package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.models.dtos.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface RegistrationService {
  ResponseEntity<?> createUser(UserDTO userDTO);
}
