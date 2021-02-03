package com.greenfoxacademy.springwebapp.globalexceptionhandling;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ErrorDTO {

  private String status;
  private String message;

  public ErrorDTO(String message) {
    this.message = message;
    this.status = "error";
  }
}
