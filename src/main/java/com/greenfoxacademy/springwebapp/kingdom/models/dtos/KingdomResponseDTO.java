package com.greenfoxacademy.springwebapp.kingdom.models.dtos;

import com.greenfoxacademy.springwebapp.buildings.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KingdomResponseDTO {

  private long id;
  private String name;
  private long userId;
  private Set<BuildingEntity> buildings;
  private Set<ResourceEntity> resources;
}
