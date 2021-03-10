package com.greenfoxacademy.springwebapp.battle.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BattleServiceImpl implements BattleService {
  private final KingdomService kingdomService;

  private int attackerAP = 0;
  private int attackerDP = 0;
  private int attackerHP = 0;
  private int defenderAP = 0;
  private int defenderDP = 0;
  private int defenderHP = 0;


  //Before Battle
  public Boolean prepareForBattle(KingdomEntity attackingKingdom, KingdomEntity defendingKingdom,
                                  List<TroopEntity> attackingArmy) {
    calculateAttackPoints(attackingArmy, defendingKingdom);
    calculateHealthPoints(attackingKingdom, attackingArmy, defendingKingdom);
    calculateDefencePoints(attackingArmy, defendingKingdom);

    return true;
  }

  public int calculateAttackPoints(List<TroopEntity> attackingArmy,
                                    KingdomEntity defendingKingdom) {

    return 0;
  }

  public int calculateHealthPoints(KingdomEntity attackingKingdom, List<TroopEntity> attackingArmy,
                                    KingdomEntity defendingKingdom) {
    int distance = calculateDistanceTraveled(attackingKingdom, defendingKingdom);

    return 0;
  }

  public int calculateDistanceTraveled(KingdomEntity attackingKingdom,
                                        KingdomEntity defendingKingdom) {

    return 0;
  }

  public int calculateDefencePoints(List<TroopEntity> attackingArmy,
                                     KingdomEntity defendingKingdom) {
    float bonusDefence = calculateBonusDefence(defendingKingdom);
    return 0;
  }

  public float calculateBonusDefence(KingdomEntity defendingKingdom) {

    return 0.0f;
  }
}
