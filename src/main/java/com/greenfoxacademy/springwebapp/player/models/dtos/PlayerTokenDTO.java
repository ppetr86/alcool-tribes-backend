package com.greenfoxacademy.springwebapp.player.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlayerTokenDTO {
    private String status;
    private String token;

    public PlayerTokenDTO(String token) {
        this.status = "ok";
        this.token = token;
    }
}