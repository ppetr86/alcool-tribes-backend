package com.greenfoxacademy.springwebapp.troop.models.dtos;

import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TroopRequestDTO {

  @Min(value=1L,message = "Provide correct buildingId!")
  private Long buildingId;
}
