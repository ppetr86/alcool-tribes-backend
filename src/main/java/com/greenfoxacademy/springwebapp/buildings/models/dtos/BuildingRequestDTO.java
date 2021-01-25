package com.greenfoxacademy.springwebapp.buildings.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildingRequestDTO {

  @NotEmpty(message = "Missing parameter(s): type!")
  private String type;
}
