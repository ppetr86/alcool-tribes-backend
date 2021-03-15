package com.greenfoxacademy.springwebapp.battle.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BattleResultDTO {
  private String winningTeam;
  private int stolenFood;
  private int stolenGold;


  public BattleResultDTO(String winningTeam) {
    this.winningTeam = winningTeam;
  }

  public BattleResultDTO(int stolenFood, int stolenGold) {
    this.stolenFood = stolenFood;
    this.stolenGold = stolenGold;
  }
}
