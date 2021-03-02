package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class AuthFactory {

  public static Authentication createAuth(String userName, Long kingdomId) {

    CustomUserDetails userDetails = new CustomUserDetails();
    PlayerEntity player = new PlayerEntity();
    player.setUsername(userName);
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setId(kingdomId);

    userDetails.setLogin(player);
    userDetails.setKingdom(kingdom);

    return new UsernamePasswordAuthenticationToken(userDetails, null, null);
  }

  public static Authentication createAuthFullKingdom(String userName, Long kingdomId) {

    CustomUserDetails userDetails = new CustomUserDetails();
    PlayerEntity player = new PlayerEntity();
    player.setUsername(userName);
    player.setId(kingdomId);
    KingdomEntity kingdom = KingdomFactory.createFullKingdom(kingdomId, kingdomId);

    userDetails.setLogin(player);
    userDetails.setKingdom(kingdom);

    return new UsernamePasswordAuthenticationToken(userDetails, null, null);
  }

}
