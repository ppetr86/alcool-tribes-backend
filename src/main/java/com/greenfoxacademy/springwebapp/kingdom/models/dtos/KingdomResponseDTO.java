package com.greenfoxacademy.springwebapp.kingdom.models.dtos;

import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingSingleResponseDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.dtos.LocationEntityDTO;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceResponseDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class KingdomResponseDTO {

  private long id;
  private String name;
  private long userId;
  private List<BuildingSingleResponseDTO> buildings;
  private List<ResourceResponseDTO> resources;
  private List<TroopEntityResponseDTO> troops;
  private LocationEntityDTO location;


  public KingdomResponseDTO(KingdomEntity e) {
    this.id = e.getId();
    this.name = e.getKingdomName();
    this.userId = e.getPlayer().getId();

    this.buildings = e.getBuildings().stream()
            .map(BuildingSingleResponseDTO::new)
            .collect(Collectors.toList());

    this.resources = e.getResources().stream()
            .map(ResourceResponseDTO::new)
            .collect(Collectors.toList());

    this.troops = e.getTroops().stream()
            .map(TroopEntityResponseDTO::new)
            .collect(Collectors.toList());

    this.location = new LocationEntityDTO(e.getLocation());
  }
}