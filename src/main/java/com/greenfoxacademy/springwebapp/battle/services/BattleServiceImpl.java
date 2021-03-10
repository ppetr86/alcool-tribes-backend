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
  private int defenderBonusDefence = 0;


  public Boolean prepareForBattle(KingdomEntity attackingKingdom, KingdomEntity defendingKingdom,
                                  List<TroopEntity> attackingArmy) {
    return true;
  }
}
