package com.greenfoxacademy.springwebapp.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingSingleResponseDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;
import com.greenfoxacademy.springwebapp.location.models.dtos.LocationEntityDTO;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceResponseDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ConvertService {

  private final ObjectMapper mapper = new ObjectMapper();

  public String objectToJson(Object o) {
    try {
      return mapper.writeValueAsString(o);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return "failed object to json conversion";
  }

  public KingdomResponseDTO convertKingdomToDTO(KingdomEntity e) {
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
}
