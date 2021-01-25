package com.greenfoxacademy.springwebapp.register.services;

import com.greenfoxacademy.springwebapp.register.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.register.models.dtos.PlayerRegistrationRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface RegistrationService {

  PlayerEntity savePlayer(PlayerRegistrationRequestDTO playerRegistrationRequestDTO);

  PlayerEntity findByUsername(String username);
}
