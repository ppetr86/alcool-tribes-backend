package com.greenfoxacademy.springwebapp.register.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDTO {
  private String status;
  private String message;

  public ErrorDTO(String message) {
    this.message = message;
    this.status = "error";
  }
}
