package com.greenfoxacademy.springwebapp.player.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlayerRequestDTO {

  @NotNull(message = "Username is required.")
  private String username;
  @NotNull(message = "Password is required.")
  @Size(min = 8, message = "Password has to contain at least 8 letters.")
  private String password;

}
