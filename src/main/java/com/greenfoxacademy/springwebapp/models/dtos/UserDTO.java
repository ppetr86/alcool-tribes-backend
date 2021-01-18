package com.greenfoxacademy.springwebapp.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

  private String username;

  private String password;

  private String email;

  private String kingdomname;
}
