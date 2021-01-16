package com.greenfoxacademy.springwebapp.models;


import com.greenfoxacademy.springwebapp.models.enums.BuildingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "buildings")
@Builder
@Valid
public class BuildingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private BuildingType type;
  private int level = 1;
  private int hp = 100;
  private long startedAt;
  private long finishedAt;
}

