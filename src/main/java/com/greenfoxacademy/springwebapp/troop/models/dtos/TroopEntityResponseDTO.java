package com.greenfoxacademy.springwebapp.troop.models.dtos;

import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TroopEntityResponseDTO {

  private Long id;
  private Integer level;
  private Integer hp;
  private Integer attack;
  private Integer defence;
  private Long startedAt;
  private Long finishedAt;

  public TroopEntityResponseDTO(TroopEntity entity) {

    this.id = entity.getId();
    this.level = entity.getLevel();
    this.hp = entity.getHp();
    this.attack = entity.getAttack();
    this.defence = entity.getDefence();
    this.startedAt = entity.getStartedAt();
    this.finishedAt = entity.getFinishedAt();
  }
}