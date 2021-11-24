package com.greenfoxacademy.springwebapp.kingdom.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KingdomNameDTO {

  @NotEmpty(message = "Missing parameter(s): name!")
  private String name;
}
