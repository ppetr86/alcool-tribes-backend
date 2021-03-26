package com.greenfoxacademy.springwebapp.battle.models;

import com.greenfoxacademy.springwebapp.battle.services.BattleServiceImp;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.TimerTask;

@Data
@AllArgsConstructor
public class BattleTimerTask extends TimerTask {

  private KingdomEntity attackingKingdom;
  private List<TroopEntity> attackingTroops;
  private KingdomEntity defendingKingdom;
  private int distance;
  private BattleServiceImp battleService;

  @Override
  public void run() {
    battleService.runBattle(this.attackingKingdom, this.attackingTroops, this.defendingKingdom, this.distance);
  }
}
