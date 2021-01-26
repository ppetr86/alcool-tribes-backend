package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerResponseDTO;
import com.greenfoxacademy.springwebapp.player.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.repositories.PlayerRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {
  private PlayerRepo playerRepo;
  private PasswordEncoder passwordEncoder;

  public PlayerServiceImpl(
      PlayerRepo playerRepo,
      PasswordEncoder passwordEncoder) {
    this.playerRepo = playerRepo;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public PlayerResponseDTO savePlayer(PlayerRegistrationRequestDTO dto) {
    KingdomEntity kingdom = assignKingdomName(dto);

    PlayerEntity playerEntity =
        new PlayerEntity(dto.getUsername(), passwordEncoder.encode(dto.getPassword()), dto.getEmail(), kingdom);
    playerRepo.save(playerEntity);
    
    PlayerResponseDTO responseDTO = assignResponseDto(playerEntity);
    return responseDTO;
  }

  private KingdomEntity assignKingdomName(PlayerRegistrationRequestDTO dto){
    KingdomEntity kingdom = new KingdomEntity();
    if (dto.getKingdomname() != null) {
      kingdom.setKingdomName(dto.getKingdomname());
    } else {
      kingdom.setKingdomName(dto.getUsername() + "'s kingdom");
    }
    return kingdom;
  }

  private PlayerResponseDTO assignResponseDto(PlayerEntity playerEntity){
    PlayerResponseDTO responseDTO = new PlayerResponseDTO();
    responseDTO.setId(playerEntity.getId());
    responseDTO.setUsername(playerEntity.getUsername());
    responseDTO.setEmail(playerEntity.getEmail());
    responseDTO.setKingdomId(playerEntity.getKingdomEntity().getId());
    responseDTO.setAvatar(playerEntity.getAvatar());
    responseDTO.setPoints(playerEntity.getPoints());
    return responseDTO;
  }


  @Override
  public PlayerEntity findByUsername(String username) {
    return playerRepo.findByUsername(username);
  }

  @Override
  public PlayerEntity loginUser(PlayerEntity playerInput) {
    return null;
  }
}
