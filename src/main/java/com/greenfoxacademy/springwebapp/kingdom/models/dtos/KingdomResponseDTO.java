package com.greenfoxacademy.springwebapp.kingdom.models.dtos;

import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingSingleResponseDTO;
import com.greenfoxacademy.springwebapp.location.models.dtos.LocationEntityDTO;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceResponseDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(setterPrefix = "with")
public class KingdomResponseDTO {

  private long id;
  private String name;
  private long userId;
  private List<BuildingSingleResponseDTO> buildings;
  private List<ResourceResponseDTO> resources;
  private List<TroopEntityResponseDTO> troops;
  private LocationEntityDTO location;

}