package com.greenfoxacademy.springwebapp.register.services;

import com.greenfoxacademy.springwebapp.register.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.register.models.dtos.PlayerDTO;
import org.springframework.stereotype.Service;

@Service
public interface RegistrationService {

  PlayerEntity saveUser(PlayerDTO playerDTO);

  PlayerEntity findByUsername(String username);
}
