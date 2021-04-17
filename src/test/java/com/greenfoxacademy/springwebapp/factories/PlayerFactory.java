package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegisterRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.enums.RoleType;

public class PlayerFactory {

  public static PlayerEntity createPlayer(Long playerID, KingdomEntity kingdom) {
    PlayerEntity pl = PlayerEntity.builder()
        .id(playerID)
        .username("testUser")
        .password("password")
        .email("test@test.com")
        .avatar("avatar")
        .points(10)
        .kingdom(kingdom)
        .isAccountVerified(true)
        .roleType(RoleType.ROLE_USER)
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
        .roleType(RoleType.ROLE_USER)
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
        .roleType(RoleType.ROLE_USER)
        .tokens(null)
        .build();
    return pl;
  }

  public static PlayerEntity createPlayer(PlayerRegisterRequestDTO rqst, KingdomEntity kingdom, boolean verified) {
    PlayerEntity player = new PlayerEntity();
    player.setEmail(rqst.getEmail());
    player.setUsername(rqst.getUsername());
    player.setKingdom(kingdom);
    player.setIsAccountVerified(verified);
    player.setRoleType(RoleType.ROLE_USER);
    return player;
  }
}
