package com.greenfoxacademy.springwebapp.player.register.services;

import com.greenfoxacademy.springwebapp.player.register.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.register.models.dtos.PlayerRegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.player.register.models.dtos.RegisterResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface RegistrationService {

  RegisterResponseDTO savePlayer(PlayerRegistrationRequestDTO playerRegistrationRequestDTO);

  PlayerEntity findByUsername(String username);
}
