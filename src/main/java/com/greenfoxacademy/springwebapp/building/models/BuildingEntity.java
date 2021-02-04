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
  private int level;
  private int hp;
  @Column(updatable = false)
  private long startedAt;
  @Column(updatable = false)
  private long finishedAt;
  @JsonIgnore
  @ManyToOne
  private KingdomEntity kingdom;

  public BuildingEntity(BuildingType type, long startedAt) {
    this.type = type;
    this.startedAt = startedAt;
  }

  public BuildingEntity(KingdomEntity kingdom, BuildingType type, int level) {
    this.type = type;
    this.level = level;
    this.kingdom = kingdom;
  }

}

