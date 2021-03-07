package com.greenfoxacademy.springwebapp.battle.models.dtos;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BattleRequestDTO {

  @NotEmpty(message = "No troop was sent to the battle!")
  private long[] troopIds;
}
