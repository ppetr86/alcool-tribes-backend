package com.greenfoxacademy.springwebapp.troop.models;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "troops")
@NoArgsConstructor
@AllArgsConstructor
public class TroopEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Integer level;
  private Integer hp;
  private Integer attack;
  private Integer defence;
  private Long startedAt;
  private Long finishedAt;
  @ManyToOne
  private KingdomEntity kingdom;

  public TroopEntity(Long id, int level, int hp, int attack,
                     int defence, long startedAt, long finishedAt) {
    this.id = id;
    this.level = level;
    this.hp = hp;
    this.attack = attack;
    this.defence = defence;
    this.startedAt = startedAt;
    this.finishedAt = finishedAt;
  }
}