package com.greenfoxacademy.springwebapp.building.models.dtos;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildingDetailsDTO {

  private long id;
  private String type;
  private int level;
  private int hp;
  private long startedAt;
  private long finishedAt;

  public BuildingDetailsDTO(BuildingEntity building) {
    this.id = building.getId();
    this.type = building.getType().toString().toLowerCase();
    this.level = building.getLevel();
    this.hp = building.getHp();
    this.startedAt = building.getStartedAt();
    this.finishedAt = building.getFinishedAt();
  }
}
