package com.greenfoxacademy.springwebapp.models.dtos;

import com.greenfoxacademy.springwebapp.models.BuildingEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class BuildingRequestDTO {

  @NotEmpty
  private String type;
}
