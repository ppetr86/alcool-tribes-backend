package com.greenfoxacademy.springwebapp.troop.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TroopResponseDto {

  private Set<TroopEntityResponseDto> troops;
}
