package com.greenfoxacademy.springwebapp.player.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerRegisterRequestDTO {

  @NotBlank(message = "Username is required.")
  private String username;

  @NotBlank(message = "Password is required.")
  @Size(min = 8, message = "Password must be 8 characters.")
  private String password;

  @Email(message = "Kindly provide valid e-mail address.")
  private String email;

  private String kingdomname;

  public PlayerRegisterRequestDTO(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }
}