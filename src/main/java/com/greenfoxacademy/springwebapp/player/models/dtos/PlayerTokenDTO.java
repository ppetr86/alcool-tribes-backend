package com.greenfoxacademy.springwebapp.player.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerTokenDTO {
  private String status;
  private String token;
}
