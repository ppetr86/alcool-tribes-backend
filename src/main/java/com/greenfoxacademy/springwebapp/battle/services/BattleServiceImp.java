package com.greenfoxacademy.springwebapp.battle.services;

import com.greenfoxacademy.springwebapp.battle.models.Army;
import com.greenfoxacademy.springwebapp.battle.models.BattleTimerTask;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class BattleServiceImp implements BattleService{

  public void scheduleBattle(KingdomEntity attackingKingdom, List<TroopEntity> attackingTroops,
                                   KingdomEntity defendingKingdom) {

    int delay = battleHasTime(attackingKingdom, defendingKingdom);

    BattleTimerTask battleTimerTask = new BattleTimerTask(attackingKingdom, attackingTroops, defendingKingdom, delay, this);
    Timer timer = new Timer();
    timer.schedule(battleTimerTask, delay);

   //TODO: this method will be replaced by different code when
    //set the troops that they are not home (later - after peter has this method ready)
  }

  public void runBattle(KingdomEntity attackingKingdom, List<TroopEntity> attackingTroops,
                         KingdomEntity defendingKingdom, int distance) {

  }

  private void modifyAttackingKingdomResources(Army attackingArmy, int foodChange, int goldChange, int distance) {

  }

  public Integer battleHasTime(KingdomEntity attackingKingdom, KingdomEntity defendingKingdom) {
    Integer attackX = attackingKingdom.getLocation().getX();
    Integer attackY = attackingKingdom.getLocation().getY();
    Integer defendX = defendingKingdom.getLocation().getX();
    Integer defendY = defendingKingdom.getLocation().getY();

    Integer differenceX = differenceBetweenTwoKingdomsLocations(attackX, defendX);
    Integer differenceY = differenceBetweenTwoKingdomsLocations(attackY, defendY);

    return differenceX + differenceY;
  }

  public Integer differenceBetweenTwoKingdomsLocations(int attackXOrY, int defendXOrY) {
    if ((attackXOrY <= 0 && defendXOrY <= 0) || (0 <= attackXOrY && 0 <= defendXOrY)){
      if (attackXOrY < defendXOrY){
        return defendXOrY - attackXOrY;
      } else {
        return attackXOrY - defendXOrY;
      }
    } else if (attackXOrY <= 0){
      return defendXOrY - attackXOrY;
    } else {
      return attackXOrY - defendXOrY;
    }
  }

  private ScheduledExecutorService getScheduledService() {
    return Executors.newSingleThreadScheduledExecutor();
  }
}
