package com.greenfoxacademy.springwebapp.battle.models;

import com.greenfoxacademy.springwebapp.battle.services.BattleServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.TimerTask;

@AllArgsConstructor
@Getter
public class ReturnHomeTimerTask extends TimerTask {

  private Army attackingArmy;
  private int foodChange;
  private int goldChange;
  private BattleServiceImpl battleService;

  @Override
  public void run() {
    battleService.modifyAttackingKingdomResources(
        this.attackingArmy, this.foodChange, this.goldChange);
  }
}
