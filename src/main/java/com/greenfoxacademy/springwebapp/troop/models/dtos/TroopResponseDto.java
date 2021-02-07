package com.greenfoxacademy.springwebapp.troop.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TroopResponseDto {

  private List<TroopEntityResponseDTO> troops;
}
