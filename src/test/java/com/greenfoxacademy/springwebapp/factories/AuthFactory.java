package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.enums.RoleType;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;

public class AuthFactory {

  public static Authentication createAuth(String userName, Long kingdomId) {
    CustomUserDetails userDetails = createUser(userName, kingdomId);
    return new UsernamePasswordAuthenticationToken(userDetails, null, null);
  }

  public static Authentication createAuthWithResources(List<ResourceEntity> resources) {
    CustomUserDetails user = createUser("user name", 1L);
    user.getKingdom().setResources(resources);
    PlayerEntity player = new PlayerEntity();
    player.setUsername("userName");
    player.setRoleType(RoleType.ROLE_USER);
    user.getKingdom().setPlayer(player);
    return new UsernamePasswordAuthenticationToken(user, null, null);
  }

  private static CustomUserDetails createUser(String userName, Long kingdomId) {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setId(kingdomId);
    kingdom.setBuildings(BuildingFactory.createDefaultLevel1BuildingsWithAllData());
    CustomUserDetails userDetails = new CustomUserDetails();

    userDetails.setKingdom(kingdom);
    return userDetails;
  }

  public static Authentication createAuthFullKingdom(String userName, Long kingdomId) {

    CustomUserDetails userDetails = new CustomUserDetails();
    PlayerEntity player = new PlayerEntity();
    player.setUsername(userName);
    player.setId(kingdomId);
    player.setRoleType(RoleType.ROLE_USER);
    KingdomEntity kingdom = KingdomFactory.createFullKingdom(kingdomId, kingdomId);

    userDetails.setLogin(player);
    userDetails.setKingdom(kingdom);


    return new UsernamePasswordAuthenticationToken(userDetails, null, null);
  }

  public static Authentication createAuth2(Long kingdomId) {

    CustomUserDetails userDetails = new CustomUserDetails();
    KingdomEntity kingdom = KingdomFactory.createFullKingdom(kingdomId, kingdomId);
    PlayerEntity player = PlayerFactory.createPlayer(3L, kingdom);

    userDetails.setLogin(player);
    userDetails.setKingdom(kingdom);

    return new UsernamePasswordAuthenticationToken(userDetails, null, null);
  }
}
