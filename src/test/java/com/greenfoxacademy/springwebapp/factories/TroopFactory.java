package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;

public class TroopFactory {

  public static TroopEntity createTroopWithID(long id) {
    TroopEntity tr = new TroopEntity();
    tr.setId(id);
    return tr;
  }
}
