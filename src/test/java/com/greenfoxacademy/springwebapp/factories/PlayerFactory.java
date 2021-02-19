package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;

public class PlayerFactory {

  public static PlayerEntity createPlayer(Long playerID, KingdomEntity kingdom) {
    PlayerEntity pl = new PlayerEntity(playerID, "testUser", "password", "test@test.com", null, null, kingdom);
    return pl;
  }
}
