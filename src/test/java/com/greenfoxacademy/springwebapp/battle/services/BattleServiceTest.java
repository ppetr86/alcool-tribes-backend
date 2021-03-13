package com.greenfoxacademy.springwebapp.battle.services;

import com.greenfoxacademy.springwebapp.battle.models.Army;
import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleRequestDTO;
import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleResponseDTO;
import com.greenfoxacademy.springwebapp.battle.models.enums.ArmyType;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.factories.TroopFactory;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import com.greenfoxacademy.springwebapp.troop.services.TroopService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BattleServiceTest {
  private KingdomService kingdomService;
  private BuildingService buildingService;
  private TroopService troopService;
  private BattleServiceImpl battleService;

  @Before
  public void init() {
    kingdomService = Mockito.mock(KingdomService.class);
    buildingService = Mockito.mock(BuildingService.class);
    troopService = Mockito.mock(TroopService.class);
    battleService = new BattleServiceImpl(kingdomService, buildingService, troopService);
    battleService = Mockito.spy(battleService);
  }

  @Test
  public void goToWarReturnsCorrectResponseDTO() {
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L,1L);
    List<TroopEntity> troops = TroopFactory.createDefaultTroops(); //3 troops with ids 1-3
    attackingKingdom.setTroops(troops);
    KingdomEntity defendingKingdom = KingdomFactory.createFullKingdom(2L,2L);
    Long[] troopsIds = {1L,2L};
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopsIds);

    Mockito.when(kingdomService.findByID(2L)).thenReturn(defendingKingdom);
    Mockito.doReturn(troops).when(battleService).getAttackingTroops(requestDTO, attackingKingdom);
    Mockito.doReturn(1).when(battleService).scheduleBattle(attackingKingdom,troops, defendingKingdom);

    BattleResponseDTO response = battleService.goToWar(2L, requestDTO, attackingKingdom);
    Assert.assertEquals("ok", response.getStatus());
    Assert.assertEquals("Battle started", response.getMessage());
  }

  @Test (expected = MissingParameterException.class)
  public void goToWar_troopIdsNotInTheKingdom_ReturnsMissingParameterException() {
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L,1L);
    List<TroopEntity> troops = TroopFactory.createDefaultTroops(); //3 troops with ids 1-3
    attackingKingdom.setTroops(troops);
    KingdomEntity enemyKingdom = KingdomFactory.createFullKingdom(2L,2L);

    Long[] troopsIds = {400L,401L}; //troops not existing in attacking kingdom
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopsIds);

    Mockito.when(kingdomService.findByID(2L)).thenReturn(enemyKingdom);

    BattleResponseDTO response = battleService.goToWar(2L, requestDTO, attackingKingdom);
  }

  @Test (expected = ForbiddenActionException.class)
  public void goToWar_bothKingdomsHaveSameId_ReturnsForbiddenActionException() {
    Long[] troopsIds = {1L,2L};
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopsIds);
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L,1L);

    BattleResponseDTO response = battleService.goToWar(1L, requestDTO, attackingKingdom);
  }

  @Test (expected = IdNotFoundException.class)
  public void goToWar_ReturnsIdNotFoundException() {
    Long[] troopsIds = {1L,2L};
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopsIds);
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L,1L);

    Mockito.when(kingdomService.findByID(2L)).thenReturn(null);

    BattleResponseDTO response = battleService.goToWar(2L, requestDTO, attackingKingdom);
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
    //checking whether army contains troop with set id or not
    Assert.assertTrue((army.stream().filter(a -> a.getId().equals(1L)).findFirst().orElse(null)) != null);
    Assert.assertTrue((army.stream().filter(a -> a.getId().equals(2L)).findFirst().orElse(null)) != null);
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
    //checking whether army contains troop with set id or not
    Assert.assertTrue((army.stream().filter(a -> a.getId().equals(1L)).findFirst().orElse(null)) != null);
    Assert.assertTrue((army.stream().filter(a -> a.getId().equals(2L)).findFirst().orElse(null)) != null);
    Assert.assertFalse((army.stream().filter(a -> a.getId().equals(10L)).findFirst().orElse(null)) != null);
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

  @Test
  public void calculateHPforAttackingArmy_returnsCorrectHP() {
    List<TroopEntity> troops = TroopFactory.createDefaultTroops();
    int distance = 10;

    int hp = battleService.calculateHPforAttackingArmy(troops,distance);

    Assert.assertEquals(245, hp);
  }

  @Test
  public void calculateHPforAttackingArmy_ArmyNotSurvivesTravel_returns0hp() {
    List<TroopEntity> troops = TroopFactory.createDefaultTroops();
    int distance = 1000;

    int hp = battleService.calculateHPforAttackingArmy(troops,distance);

    Assert.assertEquals(0, hp);
  }

  @Test
  public void killTroops_returnsIDsOfKilledTroops() {
    List<TroopEntity> troopsToBeKilled = TroopFactory.createDefaultTroops();
    List<Long> deadTroopsIds = new ArrayList<>(Arrays.asList(1L,2L,3L));

    List<Long> ids = battleService.killTroops(troopsToBeKilled);

    Assert.assertEquals(deadTroopsIds, ids);
  }

  @Test
  public void calculateAttackPoints_returnsCorrectAP() {
    List<TroopEntity> troops = TroopFactory.createDefaultTroops();

    int dp = battleService.calculateAttackPoints(troops);

    Assert.assertEquals(306, dp);
  }

  @Test
  public void calculateDPforAttackingArmy_returnsCorrectDP() {
    List<TroopEntity> troops = TroopFactory.createDefaultTroops();

    int dp = battleService.calculateDPforAttackingArmy(troops);

    Assert.assertEquals(306, dp);
  }

  @Test
  public void prepareAttackingArmy_returnsDefendingArmy() {
    List<TroopEntity> attackingTroops = TroopFactory.createDefaultTroops();
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L, 1L);
    attackingKingdom.setTroops(attackingTroops);
    int distance = 10;

    Army army = battleService.prepareAttackingArmy(attackingTroops, attackingKingdom, distance);

    Assert.assertEquals(3, army.getTroops().size());
    Assert.assertEquals(245, army.getHealthPoints());
    Assert.assertEquals(306, army.getAttackPoints());
    Assert.assertEquals(306, army.getDefencePoints());
    Assert.assertEquals(attackingKingdom, army.getKingdom());
    Assert.assertEquals(ArmyType.ATTACKINGARMY, army.getArmyType());
  }


}
