package com.greenfoxacademy.springwebapp.player.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerResponseDTO {

    private long id;
    private String username;
    private String email;
    private long kingdomId;
    private String avatar;
    private int points;

}
