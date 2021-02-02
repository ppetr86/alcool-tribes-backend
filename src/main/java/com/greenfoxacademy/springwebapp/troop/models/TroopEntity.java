package com.greenfoxacademy.springwebapp.troop.models;

import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "troops")
@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class TroopEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private int level;
  private int hp;
  private int attack;
  private int defence;
  private long startedAt;
  private long finishedAt;

  public TroopResponseDto entityToDTO(TroopEntity e) {
    TroopResponseDto result = new TroopResponseDto();
    this.level = e.getLevel();
    this.hp = e.getHp();
    this.attack = e.getAttack();
    this.defence = e.getDefence();
    this.startedAt = e.getStartedAt();
    this.finishedAt = e.getFinishedAt();
    return result;
  }
}