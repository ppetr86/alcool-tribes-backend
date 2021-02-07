package com.greenfoxacademy.springwebapp.troop.models.dtos;

import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class TroopResponseDTO {

  private int level;
  private int hp;
  private int attack;
  private int defence;
  private long startedAt;
  private long finishedAt;

  public TroopResponseDTO(TroopEntity entity) {

    this.level = entity.getLevel();
    this.hp = entity.getHp();
    this.attack = entity.getAttack();
    this.defence = entity.getDefence();
    this.startedAt = entity.getStartedAt();
    this.finishedAt = entity.getFinishedAt();
  }
}
