package com.greenfoxacademy.springwebapp.register.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO {

  private long id;
  @NotNull(message = "Username is required.")
  private String username;

  @NotNull(message = "Password is required.")
  @Size(min = 8, message = "Password must be 8 characters.")
  private String password;

  private String email;

  private String kingdomname;

  public PlayerDTO(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }

  public PlayerDTO(@NotNull(message = "Username is required.") String username,
                   @NotNull(message = "Password is required.") @Size(min = 8, message = "Password must be 8 characters.") String password,
                   String email, String kingdomname) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.kingdomname = kingdomname;
  }
}
