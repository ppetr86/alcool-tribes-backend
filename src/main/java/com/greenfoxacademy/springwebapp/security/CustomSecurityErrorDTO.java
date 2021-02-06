package com.greenfoxacademy.springwebapp.security;

import lombok.Data;

@Data
public class CustomSecurityErrorDTO {
  private String status = "error";
  private String message = "Unauthorised request.";

}
