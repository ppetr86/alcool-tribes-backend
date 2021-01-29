package com.greenfoxacademy.springwebapp.buildings.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {

  private final String status = "error";
  private String message;
}
