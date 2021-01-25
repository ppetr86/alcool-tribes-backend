package com.greenfoxacademy.springwebapp.player.register.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerRegistrationRequestDTO {

  private long id;
  @NotNull(message = "Username is required.")
  @NotBlank(message = "Username is required.")
  private String username;

  @NotNull(message = "Password is required.")
  @NotBlank(message = "Password is required.")
  @Size(min = 8, message = "Password must be 8 characters.")
  private String password;

  private String email;

  private String kingdomname;

  public PlayerRegistrationRequestDTO(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }

  public PlayerRegistrationRequestDTO(@NotNull(message = "Username is required.") String username,
                                      @NotNull(message = "Password is required.") @Size(min = 8, message = "Password must be 8 characters.") String password,
                                      String email, String kingdomname) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.kingdomname = kingdomname;
  }
}
