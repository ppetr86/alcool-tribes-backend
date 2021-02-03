package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.buildings.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.buildings.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerResponseDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.repositories.PlayerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class PlayerServiceImpl implements PlayerService {
  private PlayerRepository playerRepo;
  private PasswordEncoder passwordEncoder;

  public PlayerServiceImpl(
      PlayerRepository playerRepo,
      PasswordEncoder passwordEncoder) {
    this.playerRepo = playerRepo;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public PlayerResponseDTO saveNewPlayer(PlayerRegistrationRequestDTO dto) {
    KingdomEntity kingdom = assignKingdomName(dto);

    Set<BuildingEntity> setOfDefaultBuildings = loadBuildingsWithLevel1();
    PlayerEntity playerEntity =
        new PlayerEntity(dto.getUsername(), passwordEncoder.encode(dto.getPassword()), dto.getEmail(), setOfDefaultBuildings,
            kingdom);
    playerRepo.save(playerEntity);

    PlayerResponseDTO responseDTO = assignResponseDto(playerEntity);
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
    responseDTO.setKingdomId(playerEntity.getKingdomEntity().getId());
    responseDTO.setAvatar(playerEntity.getAvatar());
    responseDTO.setPoints(playerEntity.getPoints());
    return responseDTO;
  }

  private Set<BuildingEntity> loadBuildingsWithLevel1() {
    BuildingEntity townhall = new BuildingEntity(BuildingType.TOWNHALL, 1);
    BuildingEntity mine = new BuildingEntity(BuildingType.MINE, 1);
    BuildingEntity academy = new BuildingEntity(BuildingType.ACADEMY, 1);
    BuildingEntity farm = new BuildingEntity(BuildingType.FARM, 1);

    Set<BuildingEntity> setOfBuildings = new HashSet<>();
    setOfBuildings.add(townhall);
    setOfBuildings.add(mine);
    setOfBuildings.add(academy);
    setOfBuildings.add(farm);

    return setOfBuildings;
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
