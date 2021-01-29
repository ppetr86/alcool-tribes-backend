package com.greenfoxacademy.springwebapp.globalexceptionhandling;

import lombok.Data;

@Data
public class ExceptionResponseDTO {

  private String status;
  private String message;

  public ExceptionResponseDTO(String message) {
    this.status = "error";
    this.message = message;
  }
}
