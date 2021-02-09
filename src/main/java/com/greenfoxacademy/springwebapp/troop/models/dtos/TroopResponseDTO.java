package com.greenfoxacademy.springwebapp.troop.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TroopResponseDTO {
  private Long id;
  private Integer level;
  private Integer hp;
  private Integer attack;
  private Integer defence;
  private Long startedAt;
  private Long finishedAt;

}
