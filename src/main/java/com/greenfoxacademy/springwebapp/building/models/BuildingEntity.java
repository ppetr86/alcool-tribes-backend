package com.greenfoxacademy.springwebapp.building.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "buildings")
public class BuildingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false)
  private Long id;
  @Column(updatable = false)
  @Enumerated(EnumType.STRING)
  private BuildingType type;
  private Integer level;
  private Integer hp;
  @Column(updatable = false)
  private Long startedAt;
  @Column(updatable = false)
  private Long finishedAt;
  @JsonIgnore
  @ManyToOne
  private KingdomEntity kingdom;

  public BuildingEntity(Long id, BuildingType type, KingdomEntity kingdom) {
    this.id = id;
    this.type = type;
    this.kingdom = kingdom;
  }

  public BuildingEntity(BuildingType type, long startedAt) {
    this.type = type;
    this.startedAt = startedAt;
  }

  public BuildingEntity(KingdomEntity kingdom, BuildingType type, int level) {
    this.type = type;
    this.level = level;
    this.kingdom = kingdom;
  }

  public BuildingEntity(Long id,BuildingType type, int level, int hp, long startedAt, long finishedAt) {
    this.id = id;
    this.type = type;
    this.level = level;
    this.hp = hp;
    this.startedAt = startedAt;
    this.finishedAt = finishedAt;
  }
}

