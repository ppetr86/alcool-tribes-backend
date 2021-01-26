package com.greenfoxacademy.springwebapp.player.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ErrorMessageDTO {

  private String status;
  private String message;

  public ErrorMessageDTO(String message) {
    this.status = "error";
    this.message = message;
  }
}
