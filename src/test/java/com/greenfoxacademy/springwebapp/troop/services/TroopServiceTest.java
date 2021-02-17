package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.factories.TroopFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopListResponseDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TroopServiceTest {

  private TroopService troopService;

  @Before
  public void init() {
    troopService = new TroopServiceImpl();
  }

  @Test
  public void troopsToListDTO_ReturnsCorrectResult() {
    KingdomEntity ke = new KingdomEntity();
    ke.setTroops(TroopFactory.createDefaultKingdomWithTroops());

    TroopListResponseDto result = troopService.troopsToListDTO(ke);

    Assert.assertEquals(3, result.getTroops().size());
    Assert.assertEquals(101, (long) result.getTroops().get(0).getFinishedAt());
  }
}