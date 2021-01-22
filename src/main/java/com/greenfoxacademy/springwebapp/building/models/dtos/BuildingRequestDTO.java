package com.greenfoxacademy.springwebapp.building.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildingRequestDTO {

  @NotEmpty
  private String type;
}
