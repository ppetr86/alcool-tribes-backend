package com.greenfoxacademy.springwebapp.player.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorMessageDTO {

  private String status;
  private String message;
}
