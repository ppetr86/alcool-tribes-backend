package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.battle.models.Army;
import com.greenfoxacademy.springwebapp.battle.models.enums.ArmyType;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArmyFactory {

  public static Army createAttackingArmy() {
    Army attackingArmy = new Army(
        100,
        50,
        100,
        TroopFactory.createDefaultTroops(),
        KingdomFactory.createFullKingdom(1L, 1L),
        ArmyType.ATTACKINGARMY
    );
    //setting same troops for Army and Kingdom (which is in Army)
    List<TroopEntity> kingdomTroops = new ArrayList<>();
    kingdomTroops.addAll(attackingArmy.getTroops());
    attackingArmy.getKingdom().setTroops(kingdomTroops);

    return attackingArmy;
  }

  public static Army createDefendingArmy() {
    return new Army(
        200,
        100,
        200,
        TroopFactory.createDefaultTroops(),
        KingdomFactory.createFullKingdom(2L, 2L),
        ArmyType.DEFENDINGARMY
    );
  }

  public static Army createAttackingArmyWithProperTroops() {
    return new Army(
        100,
        50,
        100,
        TroopFactory.createTroopsWithProperDetails(),
        KingdomFactory.createFullKingdom(1L, 1L),
        ArmyType.ATTACKINGARMY
    );
  }

  public static Army createDefendingArmyWithProperTroops() {
    return new Army(
        100,
        50,
        100,
        TroopFactory.createTroopsWithProperDetails(),
        KingdomFactory.createFullKingdom(2L, 2L),
        ArmyType.DEFENDINGARMY
    );
  }

  public static List<Army> createListOf2Armies() {
    return new ArrayList<>(Arrays.asList(createAttackingArmy(), createDefendingArmy()));
  }

  public static List<Army> createListOf2ArmiesWithProperTroops() {
    return new ArrayList<>(Arrays.asList(createAttackingArmyWithProperTroops(),
        createDefendingArmyWithProperTroops()));
  }
}
