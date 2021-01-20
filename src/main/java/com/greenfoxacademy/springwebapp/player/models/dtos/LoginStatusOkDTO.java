package com.greenfoxacademy.springwebapp.player.models.dtos;

public class LoginStatusOkDTO {
  private String status;
  private String token;

  public LoginStatusOkDTO() {
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
