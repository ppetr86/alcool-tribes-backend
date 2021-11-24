package com.greenfoxacademy.springwebapp.battle.models.dtos;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BattleRequestDTO {

    @NotEmpty(message = "You cant start battle with no troops in your army!")
    private Long[] troopIds;
}
