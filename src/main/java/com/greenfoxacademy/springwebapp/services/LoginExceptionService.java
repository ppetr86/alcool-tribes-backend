package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.models.dtos.UserDTO;
import org.springframework.http.ResponseEntity;

public interface LoginExceptionService {
  ResponseEntity<?> loginExceptions(UserDTO userDTO);


}
