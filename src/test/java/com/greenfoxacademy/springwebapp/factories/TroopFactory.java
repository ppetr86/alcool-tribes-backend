package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;

import java.util.ArrayList;
import java.util.List;

public class TroopFactory {

  public static List<TroopEntity> createDefaultTroops() {
    List<TroopEntity> newTroops = new ArrayList<>();
    newTroops.add(new TroopEntity(1L, 1, 101, 101, 101, 101L, 101L, null));
    newTroops.add(new TroopEntity(2L, 2, 102, 102, 102, 102L, 102L, null));
    newTroops.add(new TroopEntity(3L, 3, 103, 103, 103, 103L, 103L, null));

    return newTroops;
  }

  public static List<TroopEntity> createTroops(KingdomEntity kingdom) {
    List<TroopEntity> newTroops = new ArrayList<>();
    newTroops.add(new TroopEntity(1L, 1, 100, 100, 100, 999L, 1111L, kingdom));
    newTroops.add(new TroopEntity(2L, 1, 100, 100, 100, 999L, 1111L, kingdom));
    return newTroops;
  }
}