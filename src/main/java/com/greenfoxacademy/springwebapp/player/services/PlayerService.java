package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.IncorrectUsernameOrPwdException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidTokenException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotVerifiedRegistrationException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.UsernameIsTakenException;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegisterRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerResponseDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import org.springframework.stereotype.Service;

@Service
public interface PlayerService {

  PlayerEntity saveNewPlayer(PlayerRegisterRequestDTO playerRegistrationRequestDTO);

  PlayerEntity findByUsername(String username);

  PlayerEntity findByUsernameAndPassword(String username, String password);

  boolean sendRegistrationConfirmationEmail(final PlayerEntity user);

  boolean verifyUser(final String token) throws InvalidTokenException;

  PlayerResponseDTO playerToResponseDTO(PlayerEntity playerEntity);

  PlayerEntity registerNewPlayer(PlayerRegisterRequestDTO request) throws UsernameIsTakenException;

  boolean existsPlayerByUsername(String username);

  PlayerTokenDTO loginPlayer(PlayerRequestDTO request)
      throws IncorrectUsernameOrPwdException, NotVerifiedRegistrationException;
}
