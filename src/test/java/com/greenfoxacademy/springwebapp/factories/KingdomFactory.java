package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;

public class KingdomFactory {

  public static KingdomEntity createKingdomEntityWithId(long l) {
    KingdomEntity ke = new KingdomEntity();
    ke.setId(l);
    return ke;
  }

  public static KingdomEntity createKingdomEntityFilledWithData(long kingdomAndUserID){
    KingdomEntity ke = createKingdomEntityWithId(kingdomAndUserID);
    ke.setTroops(TroopFactory.createDefaultTroops());
    ke.setBuildings(BuildingFactory.createDefaultBuildings());
    ke.setResources(ResourceFactory.createDefaultResources());
    return ke;
  }



}
