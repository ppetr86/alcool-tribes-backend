package com.greenfoxacademy.springwebapp.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

  private String username;

  private String password;

  private String email;

  private String kingdomname;

  public UserDTO(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }
}
