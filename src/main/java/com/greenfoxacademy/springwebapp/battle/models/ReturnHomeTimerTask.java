package com.greenfoxacademy.springwebapp.battle.models;

import com.greenfoxacademy.springwebapp.battle.services.BattleServiceImp;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.TimerTask;

@Data
@AllArgsConstructor
public class ReturnHomeTimerTask extends TimerTask {

  private Army attackingArmy;
  private int foodChange;
  private int goldChange;
  private BattleServiceImp battleService;

  @Override
  public void run() {
    battleService.modifyAttackingKingdomResources(
        this.attackingArmy, this.foodChange, this.goldChange);
  }
}
