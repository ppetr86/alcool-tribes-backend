package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.models.dtos.PlayerDTO;
import org.springframework.stereotype.Service;

@Service
public interface RegistrationService {

  PlayerEntity saveUser(PlayerDTO playerDTO);

  PlayerEntity findByUsername(String username);
}
