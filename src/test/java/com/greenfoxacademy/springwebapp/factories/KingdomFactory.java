package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;

public class KingdomFactory {

  public static KingdomEntity createKingdomEntityWithId(long l) {
    KingdomEntity ke = new KingdomEntity();
    ke.setId(l);
    return ke;
  }

  public static KingdomEntity createFullKingdom(Long kingdomID, Long userID) {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setKingdomName("testKingdom");
    kingdom.setId(kingdomID);

    PlayerEntity pl = PlayerFactory.createPlayer(userID, kingdom);
    kingdom.setPlayer(pl);

    kingdom.setBuildings(BuildingFactory.createBuildings(kingdom));
    kingdom.setLocation(new LocationEntity(1L, 10, 10));
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithHighAmount());
    kingdom.setTroops(TroopFactory.createTroops(kingdom));

    return kingdom;
  }


}
