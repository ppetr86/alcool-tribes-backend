package com.greenfoxacademy.springwebapp.troop.models.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TroopRequestDTO {

  @NotNull(message = "Building Id cannot be null!")
  @Min(value=1,message = "Building ID must be higher than 0!")
  private Long buildingId;
}