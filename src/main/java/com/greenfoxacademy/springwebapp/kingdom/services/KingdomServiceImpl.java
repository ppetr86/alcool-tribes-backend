package com.greenfoxacademy.springwebapp.kingdom.services;

import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingSingleResponseDTO;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomNameDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;
import com.greenfoxacademy.springwebapp.kingdom.repositories.KingdomRepository;
import com.greenfoxacademy.springwebapp.location.models.dtos.LocationEntityDTO;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceResponseDTO;
import com.greenfoxacademy.springwebapp.webSockets.obsolete.RestAPIController;
import com.greenfoxacademy.springwebapp.webSockets.obsolete.WebSocketController;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KingdomServiceImpl implements KingdomService {


  private final WebSocketController webSocketController;
  private final RestAPIController restAPIController;
  private final KingdomRepository kingdomRepository;

  @Override
  public KingdomEntity findByID(Long id) {
    return kingdomRepository.findById(id).orElse(null);
  }

  @Override
  public KingdomResponseDTO entityToKingdomResponseDTO(Long id) throws IdNotFoundException {
    KingdomEntity kingdom = findByID(id);
    if (kingdom == null) {
      throw new IdNotFoundException();
    }
    return convert(kingdom);
  }

  @Override
  public KingdomEntity findByPlayerId(Long id) {
    return null;
  }

  public KingdomResponseDTO convert(KingdomEntity e) {
    return KingdomResponseDTO.builder()
        .withId(e.getId())
        .withName(e.getKingdomName())
        .withUserId(e.getPlayer().getId())
        .withBuildings(e.getBuildings().stream()
            .map(BuildingSingleResponseDTO::new)
            .collect(Collectors.toList()))
        .withResources(e.getResources().stream()
            .map(ResourceResponseDTO::new)
            .collect(Collectors.toList()))
        .withTroops(e.getTroops().stream()
            .map(TroopEntityResponseDTO::new)
            .collect(Collectors.toList()))
        .withLocation(new LocationEntityDTO(e.getLocation()))
        .build();
  }

  @Override
  public KingdomEntity saveKingdom(KingdomEntity kingdom) {

    kingdomRepository.saveAndFlush(kingdom);
    kingdom = kingdomRepository.findKingdomEntityByPlayer(kingdom.getPlayer());
    webSocketController.updateMeAboutKingdom(convert(kingdom));
    restAPIController.updateMeAboutKingdom(convert(kingdom));

    kingdom = kingdomRepository.save(kingdom);
    return kingdom;
  }

  @Override
  public String findKingdomNameByPlayerID(Long id) {
    return kingdomRepository.findKingdomNameByPlayerID(id);
  }

  @Override
  public KingdomResponseDTO changeKingdomName(KingdomEntity kingdom, KingdomNameDTO nameDTO) {
    kingdom.setKingdomName(nameDTO.getName());
    saveKingdom(kingdom);
    return convert(kingdom);
  }
}