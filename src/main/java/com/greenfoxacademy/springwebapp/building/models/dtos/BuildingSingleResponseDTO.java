package com.greenfoxacademy.springwebapp.building.models.dtos;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildingSingleResponseDTO {

  private long id;
  private String type;
  private int level;
  private int hp;
  private long startedAt;
  private long finishedAt;

  public BuildingSingleResponseDTO(BuildingEntity entity) {
    this.id = entity.getId();
    this.type = entity.getType().buildingType;
    this.level = entity.getLevel();
    this.hp = entity.getHp();
    this.startedAt = entity.getStartedAt();
    this.finishedAt = entity.getFinishedAt();
  }
}
