package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegisterRequestDTO;

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
    pl.setUsername("testUsername");
    pl.setEmail("test@mail.com");
    kingdom.setPlayer(pl);

    kingdom.setBuildings(BuildingFactory.createDefaultBuildings(kingdom));
    kingdom.setLocation(new LocationEntity(1L, 10, 10, null, LocationType.KINGDOM));
    kingdom.setResources(ResourceFactory.createDefaultResources(kingdom));
    kingdom.setTroops(TroopFactory.createTroops(kingdom));

    return kingdom;
  }

  public static KingdomEntity createFullKingdom(Long kingdomID, Long userID, boolean isAccountVerified) {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setKingdomName("testKingdom");
    kingdom.setId(kingdomID);

    PlayerEntity pl = PlayerFactory.createPlayer(userID, kingdom);
    pl.setUsername("testUsername");
    pl.setEmail("test@mail.com");
    pl.setIsAccountVerified(isAccountVerified);
    kingdom.setPlayer(pl);

    kingdom.setBuildings(BuildingFactory.createDefaultBuildings(kingdom));
    kingdom.setLocation(new LocationEntity(1L, 10, 10, kingdom, LocationType.KINGDOM));
    kingdom.setResources(ResourceFactory.createDefaultResources(kingdom));
    kingdom.setTroops(TroopFactory.createTroops(kingdom));

    return kingdom;
  }

  public static KingdomEntity createFullKingdom(Long kingdomID, Long userID, boolean isAccountVerified,
                                                PlayerRegisterRequestDTO rqst) {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setKingdomName(rqst.getKingdomname());
    kingdom.setId(kingdomID);

    PlayerEntity pl = PlayerFactory.createPlayer(rqst, kingdom, isAccountVerified);
    pl.setUsername(rqst.getUsername());
    pl.setEmail("test@mail.com");
    pl.setIsAccountVerified(isAccountVerified);
    kingdom.setPlayer(pl);

    kingdom.setBuildings(BuildingFactory.createDefaultBuildings(kingdom));
    kingdom.setLocation(new LocationEntity(1L, 10, 10, kingdom, LocationType.KINGDOM));
    kingdom.setResources(ResourceFactory.createDefaultResources(kingdom));
    kingdom.setTroops(TroopFactory.createTroops(kingdom));

    return kingdom;
  }


}
