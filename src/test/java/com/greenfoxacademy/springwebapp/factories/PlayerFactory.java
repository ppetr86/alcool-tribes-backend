package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;

public class PlayerFactory {

  public static PlayerEntity createPlayer(Long playerID, KingdomEntity kingdom) {
    PlayerEntity pl = PlayerEntity.builder()
        .id(playerID)
        .username("testUser")
        .password("password")
        .email("test@test.com")
        .avatar(null)
        .points(null)
        .kingdom(kingdom)
        .isAccountVerified(true)
        .tokens(null)
        .build();
    return pl;
  }

  public static PlayerEntity createPlayer(Long playerID, KingdomEntity kingdom, boolean verified) {
    PlayerEntity pl = PlayerEntity.builder()
        .id(playerID)
        .username("testUser")
        .password("password")
        .email("test@test.com")
        .avatar(null)
        .points(null)
        .kingdom(kingdom)
        .isAccountVerified(verified)
        .tokens(null)
        .build();
    return pl;
  }

  public static PlayerEntity createPlayer(Long playerID, KingdomEntity kingdom, boolean verified, String uname) {
    PlayerEntity pl = PlayerEntity.builder()
        .id(playerID)
        .username(uname)
        .password("password")
        .email("test@test.com")
        .avatar(null)
        .points(null)
        .kingdom(kingdom)
        .isAccountVerified(verified)
        .tokens(null)
        .build();
    return pl;
  }
}
