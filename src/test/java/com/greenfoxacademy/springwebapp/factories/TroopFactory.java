package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;

import java.util.Arrays;
import java.util.List;

public class TroopFactory {

  public static List<TroopEntity> createDefaultTroops() {
    return (Arrays.asList(
        new TroopEntity(1L, 1, 101, 101, 101, 101L, 101L, null),
        new TroopEntity(2L, 2, 102, 102, 102, 102L, 102L, null),
        new TroopEntity(3L, 3, 103, 103, 103, 103L, 103L, null)));
  }

  public static List<TroopEntity> createTroops(KingdomEntity kingdom) {
    return (Arrays.asList(
        new TroopEntity(1L, 1, 100, 100, 100, 999L, 1111L, kingdom),
        new TroopEntity(2L, 1, 100, 100, 100, 999L, 1111L, kingdom)));
  }
}