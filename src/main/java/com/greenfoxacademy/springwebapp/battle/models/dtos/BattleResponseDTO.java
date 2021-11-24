package com.greenfoxacademy.springwebapp.battle.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BattleResponseDTO {
    private String status;
    private String message;

    public BattleResponseDTO() {
        this.message = "Battle started";
        this.status = "ok";
    }
}
