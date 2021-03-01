package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerListResponseDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface PlayerService {

  PlayerResponseDTO saveNewPlayer(PlayerRegistrationRequestDTO playerRegistrationRequestDTO);

  PlayerEntity findByUsername(String username);

  PlayerEntity findByUsernameAndPassword(String username, String password);

  PlayerListResponseDTO findPlayersAroundMe(KingdomEntity kingdom);
}
