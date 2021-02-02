package com.greenfoxacademy.springwebapp.troop.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TroopEntityResponseDto {

  private int level;
  private int hp;
  private int attack;
  private int defence;
  private long startedAt;
  private long finishedAt;
}