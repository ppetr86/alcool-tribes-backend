package com.greenfoxacademy.springwebapp.register.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO {

  private String username;

  private String password;

  private String email;

  private String kingdomname;

  public PlayerDTO(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }
}
