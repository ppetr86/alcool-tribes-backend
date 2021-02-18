package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerResponseDTO;
import com.greenfoxacademy.springwebapp.player.repositories.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {
  private final PlayerRepository playerRepo;
  private final PasswordEncoder passwordEncoder;
  private final BuildingService buildingService;

  @Override
  public PlayerResponseDTO saveNewPlayer(PlayerRegistrationRequestDTO dto) {
    KingdomEntity kingdom = assignKingdomName(dto);
    List<BuildingEntity> defaultBuildings = buildingService.createDefaultBuildings(kingdom);
    kingdom.setBuildings(defaultBuildings);

    PlayerEntity player =
            new PlayerEntity(dto.getUsername(), passwordEncoder.encode(dto.getPassword()), dto.getEmail());
    player.setKingdom(kingdom);
    kingdom.setPlayer(player);
    playerRepo.save(player);

    PlayerResponseDTO responseDTO = assignResponseDto(player);
    return responseDTO;
  }


  private KingdomEntity assignKingdomName(PlayerRegistrationRequestDTO dto) {
    KingdomEntity kingdom = new KingdomEntity();
    if (dto.getKingdomname() != null) {
      kingdom.setKingdomName(dto.getKingdomname());
    } else {
      kingdom.setKingdomName(dto.getUsername() + "'s kingdom");
    }
    return kingdom;
  }

  private PlayerResponseDTO assignResponseDto(PlayerEntity playerEntity) {
    PlayerResponseDTO responseDTO = new PlayerResponseDTO();
    responseDTO.setId(playerEntity.getId());
    responseDTO.setUsername(playerEntity.getUsername());
    responseDTO.setEmail(playerEntity.getEmail());
    responseDTO.setAvatar(playerEntity.getAvatar());
    responseDTO.setPoints(playerEntity.getPoints());
    responseDTO.setKingdomId(playerEntity.getKingdom().getId());
    return responseDTO;
  }

  @Override
  public PlayerEntity findByUsername(String username) {
    return playerRepo.findByUsername(username);
  }

  @Override
  public PlayerEntity findByUsernameAndPassword(String username, String password) {
    PlayerEntity playerEntity = findByUsername(username);

    if (playerEntity != null) {
      if (passwordEncoder.matches(password, playerEntity.getPassword())) {
        return playerEntity;
      }
    }
    return null;
  }
}