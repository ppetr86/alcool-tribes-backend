package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.UserDTO;
import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
import org.springframework.stereotype.Service;

@Service
public class LoginExceptionServiceImp implements LoginExceptionService {

  private PlayerEntityService playerEntityService;
  private JwtProvider jwtProvider;

  public LoginExceptionServiceImp(PlayerEntityService playerEntityService, JwtProvider jwtProvider) {
    this.playerEntityService = playerEntityService;
    this.jwtProvider = jwtProvider;
  }

  @Override
  public ErrorMessageDTO loginExceptions(String error, String msg) {
    ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO();
    errorMessageDTO.setStatus(error);
    errorMessageDTO.setMessage(msg);
    return errorMessageDTO;
  }

  @Override
  public PlayerTokenDTO generateTokenToLoggedInPlayer(UserDTO userDTO) {
    PlayerEntity loggedPlayer = playerEntityService.findByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
    String token = jwtProvider.generateToken(loggedPlayer.getUsername());
    return new PlayerTokenDTO("ok", token);
  }
}
