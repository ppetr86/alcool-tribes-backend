package com.greenfoxacademy.springwebapp.kingdom.models.dtos;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KingdomResponseDTO {

  private long id;
  private String name;
  private long userId;
  private Set<BuildingEntity> buildings;
  private Set<ResourceEntity> resources;
  private Set<TroopEntity> troops;
  private LocationEntity location;


  public KingdomResponseDTO(KingdomEntity e) {
    this.id = e.getId();
    this.name = e.getKingdomName();
    this.userId = e.getPlayer().getId();

    this.buildings = e.getBuildings();
    this.resources = e.getResources();
    this.troops = e.getTroops();
    this.location = e.getLocation();
  }
}
