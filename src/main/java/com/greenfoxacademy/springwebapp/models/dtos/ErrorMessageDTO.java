package com.greenfoxacademy.springwebapp.models.dtos;

public class ErrorMessageDTO {

  private String status;
  private String message;

  public ErrorMessageDTO() {
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
