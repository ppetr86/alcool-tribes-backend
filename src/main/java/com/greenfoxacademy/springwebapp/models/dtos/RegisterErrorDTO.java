package com.greenfoxacademy.springwebapp.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterErrorDTO {
  private String status;
  private String message;

  public RegisterErrorDTO(String message) {
    this.message = message;
    this.status = "error";
  }
}
