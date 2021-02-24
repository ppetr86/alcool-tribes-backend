package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidTokenException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.UsernameIsTakenException;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegisterRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerResponseDTO;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Service
public interface PlayerService {

  PlayerEntity saveNewPlayer(PlayerRegisterRequestDTO playerRegistrationRequestDTO);

  PlayerEntity findByUsername(String username);

  PlayerEntity findByUsernameAndPassword(String username, String password);

  boolean findIsVerified(String username);

  void sendRegistrationConfirmationEmail(final PlayerEntity user);

  boolean verifyUser(final String token) throws InvalidTokenException;

  PlayerResponseDTO playerToResponseDTO(PlayerEntity playerEntity);

  PlayerEntity registerNewPlayer(PlayerRegisterRequestDTO request) throws UsernameIsTakenException;

  boolean existsByUsername(String username);
}
