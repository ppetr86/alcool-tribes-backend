package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerListResponseDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerResponseDTO;
import com.greenfoxacademy.springwebapp.player.repositories.PlayerRepository;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {
  private final PlayerRepository playerRepo;
  private final PasswordEncoder passwordEncoder;
  private final BuildingService buildingService;
  private final ResourceService resourceService;
  private final KingdomService kingdomService;

  @Override
  public PlayerResponseDTO saveNewPlayer(PlayerRegistrationRequestDTO dto) {
    KingdomEntity kingdom = assignKingdomName(dto);
    List<BuildingEntity> defaultBuildings = buildingService.createDefaultBuildings(kingdom);
    kingdom.setBuildings(defaultBuildings);

    List<ResourceEntity> defaultResources = resourceService.createDefaultResources(kingdom);
    kingdom.setResources(defaultResources);

    PlayerEntity player =
        new PlayerEntity(dto.getUsername(), passwordEncoder.encode(dto.getPassword()), dto.getEmail());
    if (dto.getEmail() == null) {
      player.setEmail("");
    }
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

  @Override
  public PlayerListResponseDTO findPlayersAroundMe(KingdomEntity kingdom, Integer distance) {
    List<PlayerResponseDTO> playerResponseDTO = new ArrayList<>();
    List<PlayerEntity> allPlayers = (List<PlayerEntity>) playerRepo.findAll();

    if (distance == null) {
      playerResponseDTO = allPlayers.stream()
          .map(e -> assignResponseDto(e))
          .forEach(e -> playerResponseDTO.add(e))
          .collect(Collectors.toList());
    } else {
      List<KingdomEntity> kingdomEntities = allPlayers.stream()
          .map(e -> e.getKingdom())
          .filter(e -> !e.getId().equals(kingdom.getId()))
          .filter(x -> isWithinGrid(kingdom, distance, x))
          .collect(Collectors.toList());

      for (KingdomEntity kingdomEntity : kingdomEntities) {
        PlayerEntity playerEntity = kingdomEntity.getPlayer();
        PlayerResponseDTO responseDTO = assignResponseDto(playerEntity);
        playerResponseDTO.add(responseDTO);
      }

    }
    PlayerListResponseDTO playerListResponseDTO = new PlayerListResponseDTO(playerResponseDTO);
    return playerListResponseDTO;
  }

  private boolean isWithinGrid(KingdomEntity callerKingdom, Integer distance, KingdomEntity passiveKingdom) {
    return passiveKingdom.getLocation().getX() > callerKingdom.getLocation().getX() - distance
        && passiveKingdom.getLocation().getX() < callerKingdom.getLocation().getX() + distance
        && passiveKingdom.getLocation().getY() > callerKingdom.getLocation().getY() - distance
        && passiveKingdom.getLocation().getY() < callerKingdom.getLocation().getY() + distance;
  }

}