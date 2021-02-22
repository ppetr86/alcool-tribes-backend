package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegisterRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface PlayerService {

  PlayerResponseDTO saveNewPlayer(PlayerRegisterRequestDTO playerRegistrationRequestDTO);

  PlayerEntity findByUsername(String username);

  PlayerEntity findByUsernameAndPassword(String username, String password);

  boolean findIsVerified(String username);
}
