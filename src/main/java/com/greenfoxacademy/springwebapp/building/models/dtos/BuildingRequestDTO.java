package com.greenfoxacademy.springwebapp.building.models.dtos;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildingRequestDTO {

  @NotEmpty(message = "Missing parameter(s): type!")
  private String type;
}
