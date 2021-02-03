package com.greenfoxacademy.springwebapp.building.models;


import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "buildings")
public class BuildingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false)
  private Long id;
  @Enumerated(EnumType.STRING)
  @Column(updatable = false)
  private BuildingType type;
  private int level;
  private int hp;
  @Column(updatable = false)
  private long startedAt;
  @Column(updatable = false)
  private long finishedAt;

  public BuildingEntity(BuildingType type) {
    this.type = type;
  }

  public BuildingEntity(BuildingType type, long startedAt) {
    this.type = type;
    this.startedAt = startedAt;
  }

  public BuildingEntity(BuildingType type, int hp, long startedAt) {
    this.type = type;
    this.hp = hp;
    this.startedAt = startedAt;
  }

  public BuildingEntity(BuildingType type, int level) {
    this.type = type;
    this.level = level;
  }
}

