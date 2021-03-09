package com.greenfoxacademy.springwebapp.battle.services;

import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleRequestDTO;
import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.factories.TroopFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BattleServiceTest {
  private KingdomService kingdomService;
  private BattleServiceImpl battleService;

  @Before
  public void init() {
    kingdomService = Mockito.mock(KingdomService.class);
    battleService = new BattleServiceImpl (kingdomService);
    battleService = Mockito.spy(battleService);
  }

  @Test
  public void getAttackingArmyReturnsCorrectArmy() {
    Long[] troopsIds = {1L,2L};
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopsIds);
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L,1L);
    List<TroopEntity> troops = TroopFactory.createDefaultTroops(); //3 troops with ids 1-3
    attackingKingdom.setTroops(troops);

    List<TroopEntity> army = battleService.getAttackingArmy(requestDTO,attackingKingdom);

    Assert.assertEquals(2, army.size());
  }

  @Test
  public void getAttackingArmy_OneNonExistingId_StillReturnsCorrectArmy() {
    Long[] troopsIds = {1L,2L,10L};
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopsIds);
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L,1L);
    List<TroopEntity> troops = TroopFactory.createDefaultTroops(); //3 troops with ids 1-3
    attackingKingdom.setTroops(troops);

    List<TroopEntity> army = battleService.getAttackingArmy(requestDTO,attackingKingdom);

    Assert.assertEquals(2, army.size());
  }

  @Test
  public void getAttackingArmy_OnlyNonExistingIds_Returns0SizeArmy() {
    Long[] troopsIds = {4L,5L,6L};
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopsIds);
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L,1L);
    List<TroopEntity> troops = TroopFactory.createDefaultTroops(); //3 troops with ids 1-3
    attackingKingdom.setTroops(troops);

    List<TroopEntity> army = battleService.getAttackingArmy(requestDTO,attackingKingdom);

    Assert.assertEquals(0, army.size());
  }


}
