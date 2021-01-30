package com.greenfoxacademy.springwebapp.player.services;


import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerResponseDTO;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import org.springframework.stereotype.Service;

@Service
public interface PlayerService {

  PlayerResponseDTO saveNewPlayer(PlayerRegistrationRequestDTO playerRegistrationRequestDTO);

  PlayerEntity findByUsername(String username);
  PlayerEntity findByUsernameAndPassword(String username, String password);

}
