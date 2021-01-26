package com.greenfoxacademy.springwebapp.buildings.models;


import com.greenfoxacademy.springwebapp.buildings.models.enums.BuildingType;
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
  private Integer id;
  @Column(updatable = false)
  private BuildingType type;
  private int level = 1;
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
}

