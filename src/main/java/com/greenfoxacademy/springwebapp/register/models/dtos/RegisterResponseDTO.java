package com.greenfoxacademy.springwebapp.register.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponseDTO {

  private long id;
  private String username;
  private String email;
  private long kingdomId;
  private String avatar;
  private int points;

}
