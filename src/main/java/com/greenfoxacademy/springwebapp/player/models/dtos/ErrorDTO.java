package com.greenfoxacademy.springwebapp.player.models.dtos;

import lombok.Data;

@Data
public class ErrorDTO {
  private String status;
  private String message;

  public ErrorDTO(String message) {
    this.message = message;
    this.status = "error";
  }
}
