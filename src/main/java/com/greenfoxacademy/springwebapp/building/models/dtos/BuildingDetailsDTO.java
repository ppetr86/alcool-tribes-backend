package com.greenfoxacademy.springwebapp.building.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildingDetailsDTO {

  private long id;
  private String type;
  private int level;
  private int hp;
  private long startedAt;
  private long finishedAt;
}
