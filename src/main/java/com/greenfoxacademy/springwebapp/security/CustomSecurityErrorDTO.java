package com.greenfoxacademy.springwebapp.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomSecurityErrorDTO {
  private String status;
  private String message;

}
