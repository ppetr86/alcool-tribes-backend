package com.greenfoxacademy.springwebapp.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserErrorDTO {
  private String status;
  private String message;

  public UserErrorDTO(String message) {
    this.message = message;
    this.status = "error";
  }
}
