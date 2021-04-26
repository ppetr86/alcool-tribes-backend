package com.greenfoxacademy.springwebapp.battle.models.dtos;

import lombok.Data;

@Data
public class BattleResponseDTO {
  private String status;
  private String message;

  public BattleResponseDTO() {
    this.message = "Battle started";
    this.status = "ok";
  }
}
