package com.greenfoxacademy.springwebapp.battle.services;

import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleRequestDTO;
import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleResponseDTO;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.factories.TroopFactory;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BattleServiceTest {
  private KingdomService kingdomService;
  private BuildingService buildingService;
  private BattleServiceImpl battleService;

  @Before
  public void init() {
    kingdomService = Mockito.mock(KingdomService.class);
    buildingService = Mockito.mock(BuildingService.class);
    battleService = new BattleServiceImpl(kingdomService, buildingService);
    battleService = Mockito.spy(battleService);
  }

  @Test
  public void initiateBattleReturnsCorrectResponseDTO() {
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L,1L);
    List<TroopEntity> troops = TroopFactory.createDefaultTroops(); //3 troops with ids 1-3
    attackingKingdom.setTroops(troops);
    KingdomEntity enemyKingdom = KingdomFactory.createFullKingdom(2L,2L);
    Long[] troopsIds = {1L,2L};
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopsIds);

    Mockito.when(kingdomService.findByID(2L)).thenReturn(enemyKingdom);
    Mockito.doReturn(null).when(battleService)
        .prepareAttackingArmy(requestDTO,attackingKingdom,1);
    Mockito.doReturn(null).when(battleService)
        .prepareDefendingArmy(enemyKingdom);

    BattleResponseDTO response = battleService.initiateBattle(2L, requestDTO, attackingKingdom,1);
    Assert.assertEquals("ok", response.getStatus());
    Assert.assertEquals("Battle started", response.getMessage());
  }

  @Test (expected = MissingParameterException.class)
  public void initiateBattle_troopIdsNotInTheKingdom_ReturnsMissingParameterException() {
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L,1L);
    List<TroopEntity> troops = TroopFactory.createDefaultTroops(); //3 troops with ids 1-3
    attackingKingdom.setTroops(troops);
    KingdomEntity enemyKingdom = KingdomFactory.createFullKingdom(2L,2L);

    Long[] troopsIds = {400L,401L}; //troops not existing in attacking kingdom
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopsIds);

    Mockito.when(kingdomService.findByID(2L)).thenReturn(enemyKingdom);

    BattleResponseDTO response = battleService.initiateBattle(2L, requestDTO, attackingKingdom, 1);
  }

  @Test (expected = ForbiddenActionException.class)
  public void initiateBattle_bothKingdomsHaveSameId_ReturnsForbiddenActionException() {
    Long[] troopsIds = {1L,2L};
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopsIds);
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L,1L);

    BattleResponseDTO response = battleService.initiateBattle(1L, requestDTO, attackingKingdom, 1);
  }

  @Test (expected = IdNotFoundException.class)
  public void initiateBattle_ReturnsIdNotFoundException() {
    Long[] troopsIds = {1L,2L};
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopsIds);
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L,1L);

    Mockito.when(kingdomService.findByID(2L)).thenReturn(null);

    BattleResponseDTO response = battleService.initiateBattle(2L, requestDTO, attackingKingdom, 1);
  }

  @Test
  public void getAttackingTroopsReturnsCorrectTroops() {
    Long[] troopsIds = {1L,2L};
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopsIds);
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L,1L);
    List<TroopEntity> troops = TroopFactory.createDefaultTroops(); //3 troops with ids 1-3
    attackingKingdom.setTroops(troops);

    List<TroopEntity> army = battleService.getAttackingTroops(requestDTO,attackingKingdom);

    Assert.assertEquals(2, army.size());
  }

  @Test
  public void getAttackingTroops_OneNonExistingId_StillReturnsCorrectTroops() {
    Long[] troopsIds = {1L,2L,10L};
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopsIds);
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L,1L);
    List<TroopEntity> troops = TroopFactory.createDefaultTroops(); //3 troops with ids 1-3
    attackingKingdom.setTroops(troops);

    List<TroopEntity> army = battleService.getAttackingTroops(requestDTO,attackingKingdom);

    Assert.assertEquals(2, army.size());
  }

  @Test
  public void getAttackingTroops_OnlyNonExistingIds_Returns0SizeTroopsList() {
    Long[] troopsIds = {4L,5L,6L};
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopsIds);
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L,1L);
    List<TroopEntity> troops = TroopFactory.createDefaultTroops(); //3 troops with ids 1-3
    attackingKingdom.setTroops(troops);

    List<TroopEntity> army = battleService.getAttackingTroops(requestDTO,attackingKingdom);

    Assert.assertEquals(0, army.size());
  }

}
