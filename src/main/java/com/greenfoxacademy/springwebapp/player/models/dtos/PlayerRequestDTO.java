package com.greenfoxacademy.springwebapp.player.models.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlayerRequestDTO {

    @NotNull(message = "Username is required.")
    private String username;
    @NotNull(message = "Password is required.")
    @Size(min = 8, message = "Password has to contain at least 8 letters.")
    private String password;

}
