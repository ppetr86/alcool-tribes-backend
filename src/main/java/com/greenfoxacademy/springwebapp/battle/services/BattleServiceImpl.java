package com.greenfoxacademy.springwebapp.battle.services;

import com.greenfoxacademy.springwebapp.battle.models.Army;
import com.greenfoxacademy.springwebapp.battle.models.BattleTimerTask;
import com.greenfoxacademy.springwebapp.battle.models.ReturnHomeTimerTask;
import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleResultDTO;
import com.greenfoxacademy.springwebapp.battle.models.enums.ArmyType;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Timer;

@Service
public class BattleServiceImpl implements BattleService {

  public void scheduleBattle(KingdomEntity attackingKingdom, List<TroopEntity> attackingTroops,
                                   KingdomEntity defendingKingdom, int distance) {

    BattleTimerTask battleTimerTask = new BattleTimerTask(
        attackingKingdom, attackingTroops, defendingKingdom, distance, this);
    Timer timer = createTimer();
    timer.schedule(battleTimerTask, distance);

    //TODO: this method will be replaced by different code when
    //set the troops that they are not home (later - after peter has this method ready)
  }

  public void runBattle(KingdomEntity attackingKingdom, List<TroopEntity> attackingTroops,
                         KingdomEntity defendingKingdom, int distance) {

  }

  public BattleResultDTO performAfterBattleActions(List<Army> armiesAfterBattle, int distance) {
    Army attackingArmy = getArmyByType(armiesAfterBattle, ArmyType.ATTACKINGARMY);
    Army defendingArmy = getArmyByType(armiesAfterBattle, ArmyType.DEFENDINGARMY);

    int stolenFood = 0;
    int stolenGold = 0;
    scheduleReturnHome(attackingArmy, stolenFood, stolenGold, distance);
    return new BattleResultDTO("Nobody won", stolenFood, stolenGold);
  }

  private void scheduleReturnHome(Army attackingArmy, int foodChange, int goldChange, int distance) {
    ReturnHomeTimerTask timerTask = new ReturnHomeTimerTask(attackingArmy, foodChange, goldChange, this);
    Timer timer = createTimer();
    timer.schedule(timerTask, distance);
  }

  public Timer createTimer() {
    return new Timer();
  }

  public void modifyAttackingKingdomResources(Army attackingArmy, int foodChange, int goldChange) {

  }

  public Army getArmyByType(List<Army> armiesAfterBattle, ArmyType type) {
    return armiesAfterBattle.stream()
        .filter(army -> army.getArmyType().equals(type))
        .findFirst()
        .orElse(null);
  }
}
