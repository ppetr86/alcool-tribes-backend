package com.greenfoxacademy.springwebapp.player.models.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerTokenDTO {
  private String status;
  private String token;

  public PlayerTokenDTO(String token) {
    this.status = "ok";
    this.token = token;
  }
}
