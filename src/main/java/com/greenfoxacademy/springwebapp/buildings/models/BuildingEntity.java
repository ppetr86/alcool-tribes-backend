package com.greenfoxacademy.springwebapp.buildings.models;


import com.greenfoxacademy.springwebapp.buildings.models.enums.BuildingType;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "buildings")
@Valid
public class BuildingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false)
  private Integer id;
  @Column(updatable = false)
  private BuildingType type;
  private int level = 1;
  private int hp = 100;
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
}

