package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.FileStorageException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidTokenException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.WrongContentTypeException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.DeletedPlayerDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerListResponseDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegisterRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerResponseDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface PlayerService {

  PlayerEntity saveNewPlayer(PlayerRegisterRequestDTO playerRegistrationRequestDTO);

  PlayerEntity findByUsername(String username);

  PlayerEntity findById(Long id);

  PlayerEntity findByUsernameAndPassword(String username, String password);

  PlayerListResponseDTO findPlayersAroundMe(KingdomEntity kingdom, Integer distance);

  boolean sendRegistrationConfirmationEmail(final PlayerEntity user);

  boolean verifyUser(final String token) throws InvalidTokenException;

  PlayerResponseDTO playerToResponseDTO(PlayerEntity playerEntity);

  PlayerEntity registerNewPlayer(PlayerRegisterRequestDTO request) throws RuntimeException;

  boolean existsPlayerByUsername(String username);

  PlayerTokenDTO loginPlayer(PlayerRequestDTO request)
      throws RuntimeException;

  DeletedPlayerDTO deletePlayer(Long deletedPlayerId);


  PlayerEntity savePlayer(PlayerEntity player);

  PlayerEntity setAvatar(PlayerEntity player, MultipartFile file) throws FileStorageException,
      WrongContentTypeException;

}
