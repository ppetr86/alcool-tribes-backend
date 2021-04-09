package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.battle.models.Army;
import com.greenfoxacademy.springwebapp.battle.models.enums.ArmyType;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArmyFactory {

  public static Army createArmy(int ap, int dp, int hp, List<TroopEntity> troops, KingdomEntity kingdom, ArmyType type) {
    return new Army(ap, dp, hp, troops, kingdom, type);
  }

  public static Army createAttackingArmy(long id) {
    Army weekArmy = createArmy(
        100,
        50,
        100,
        TroopFactory.createTroopsWithProperDetails(),
        KingdomFactory.createFullKingdom(id, id),
        ArmyType.ATTACKINGARMY
    );
    //setting same troops for Army and Kingdom (purposely using different List)
    List<TroopEntity> kingdomTroops = new ArrayList<>();
    kingdomTroops.addAll(weekArmy.getTroops());
    weekArmy.getKingdom().setTroops(kingdomTroops);

    return weekArmy;
  }

  public static Army createDefendingArmy(long id) {
    Army strongArmy = createArmy(
        200,
        100,
        200,
        TroopFactory.createTroopsWithProperDetails(),
        KingdomFactory.createFullKingdom(id, id),
        ArmyType.DEFENDINGARMY
    );
    //setting same troops for Army and Kingdom (purposely using different List)
    List<TroopEntity> kingdomTroops = new ArrayList<>();
    kingdomTroops.addAll(strongArmy.getTroops());
    strongArmy.getKingdom().setTroops(kingdomTroops);

    return strongArmy;
  }

  public static List<Army> createArmiesForBattle() {
    return Arrays.asList(
        createAttackingArmy(1L),
        createDefendingArmy(2L)
    );
  }
}
