package com.greenfoxacademy.springwebapp.player.models.dtos;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerListResponseDTO {

    private List<PlayerResponseDTO> players;

    public PlayerListResponseDTO(List<PlayerResponseDTO> players) {
        this.players = players;
    }
}
