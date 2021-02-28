package com.greenfoxacademy.springwebapp.troop.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TroopRequestDTO {

  @NotNull(message = "Building Id cannot be null!")
  @Min(value = 1, message = "Building ID must be higher than 0!")
  private Long buildingId;
}
