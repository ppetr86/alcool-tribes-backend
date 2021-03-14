package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.battle.models.Army;
import com.greenfoxacademy.springwebapp.battle.models.enums.ArmyType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArmyFactory {

  public static Army createAttackingArmy() {
    return new Army(
        100,
        50,
        100,
        TroopFactory.createDefaultTroops(),
        KingdomFactory.createFullKingdom(1L,1L),
        ArmyType.ATTACKINGARMY
    );
  }

  public static Army createDefendingArmy() {
    return new Army(
        100,
        50,
        100,
        TroopFactory.createDefaultTroops(),
        KingdomFactory.createFullKingdom(2L,2L),
        ArmyType.DEFENDINGARMY
    );
  }

  public static List<Army> createListOf2Armies() {
    return new ArrayList<>(Arrays.asList(createAttackingArmy(),createDefendingArmy()));
  }
}
