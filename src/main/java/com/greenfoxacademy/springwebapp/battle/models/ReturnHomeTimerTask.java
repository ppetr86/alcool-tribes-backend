package com.greenfoxacademy.springwebapp.battle.models;

import com.greenfoxacademy.springwebapp.battle.services.BattleServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.TimerTask;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
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
