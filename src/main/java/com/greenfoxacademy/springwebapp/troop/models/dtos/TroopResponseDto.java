package com.greenfoxacademy.springwebapp.troop.models.dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public class TroopResponseDto {

  private Set<TroopEntityResponseDto> troops;
}
