package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.battle.models.Army;
import com.greenfoxacademy.springwebapp.battle.models.enums.ArmyType;
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
        KingdomFactory.createFullKingdom(1L,1L),
        ArmyType.ATTACKINGARMY
    );
    //setting same troops for Army and Kingdom (which is in Army)
    attackingArmy.getKingdom().setTroops(attackingArmy.getTroops());

    return attackingArmy;
  }

  public static Army createDefendingArmy() {
    Army defendingArmy = new Army(
        200,
        100,
        200,
        TroopFactory.createDefaultTroops(),
        KingdomFactory.createFullKingdom(2L,2L),
        ArmyType.DEFENDINGARMY
    );
    //setting same troops for Army and Kingdom (which is in Army)
    defendingArmy.getKingdom().setTroops(defendingArmy.getTroops());
    return defendingArmy;
  }

  public static List<Army> createListOf2Armies() {
    return new ArrayList<>(Arrays.asList(createAttackingArmy(),createDefendingArmy()));
  }
}
