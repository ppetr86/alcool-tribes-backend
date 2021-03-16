package com.greenfoxacademy.springwebapp.battle.services;

import com.greenfoxacademy.springwebapp.TestConfig;
import com.greenfoxacademy.springwebapp.battle.models.Army;
import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleRequestDTO;
import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleResponseDTO;
import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleResultDTO;
import com.greenfoxacademy.springwebapp.battle.models.enums.ArmyType;
import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.factories.ArmyFactory;
import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.factories.ResourceFactory;
import com.greenfoxacademy.springwebapp.factories.TroopFactory;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.enums.ResourceType;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import com.greenfoxacademy.springwebapp.troop.services.TroopService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

public class BattleServiceTest {
  private KingdomService kingdomService;
  private BuildingService buildingService;
  private TroopService troopService;
  private ResourceService resourceService;
  private BattleServiceImpl battleService;

  @Before
  public void init() {
    kingdomService = Mockito.mock(KingdomService.class);
    buildingService = Mockito.mock(BuildingService.class);
    troopService = Mockito.mock(TroopService.class);
    Environment env = TestConfig.mockEnvironment();
    resourceService = Mockito.mock(ResourceService.class);
    battleService = new BattleServiceImpl(kingdomService, buildingService, troopService, env, resourceService);
    battleService = Mockito.spy(battleService);
  }

  @Test
  public void warReturnsCorrectResponseDTO() {
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L,1L);
    List<TroopEntity> troops = TroopFactory.createDefaultTroops(); //3 troops with ids 1-3
    attackingKingdom.setTroops(troops);
    KingdomEntity defendingKingdom = KingdomFactory.createFullKingdom(2L,2L);
    Long[] troopsIds = {1L,2L};
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopsIds);

    Mockito.when(kingdomService.findByID(2L)).thenReturn(defendingKingdom);
    Mockito.doReturn(troops).when(battleService).getAttackingTroops(requestDTO, attackingKingdom);
    Mockito.doReturn(1).when(battleService).travelTime(attackingKingdom,troops, defendingKingdom);

    BattleResponseDTO response = battleService.war(2L, requestDTO, attackingKingdom);
    Assert.assertEquals("ok", response.getStatus());
    Assert.assertEquals("Battle started", response.getMessage());
  }

  @Test (expected = MissingParameterException.class)
  public void war_troopIdsNotInTheKingdom_ReturnsMissingParameterException() {
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L,1L);
    List<TroopEntity> troops = TroopFactory.createDefaultTroops(); //3 troops with ids 1-3
    attackingKingdom.setTroops(troops);
    KingdomEntity enemyKingdom = KingdomFactory.createFullKingdom(2L,2L);

    Long[] troopsIds = {400L,401L}; //troops not existing in attacking kingdom
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopsIds);

    Mockito.when(kingdomService.findByID(2L)).thenReturn(enemyKingdom);

    BattleResponseDTO response = battleService.war(2L, requestDTO, attackingKingdom);
  }

  @Test (expected = ForbiddenActionException.class)
  public void war_bothKingdomsHaveSameId_ReturnsForbiddenActionException() {
    Long[] troopsIds = {1L,2L};
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopsIds);
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L,1L);

    BattleResponseDTO response = battleService.war(1L, requestDTO, attackingKingdom);
  }

  @Test (expected = IdNotFoundException.class)
  public void war_ReturnsIdNotFoundException() {
    Long[] troopsIds = {1L,2L};
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopsIds);
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L,1L);

    Mockito.when(kingdomService.findByID(2L)).thenReturn(null);

    BattleResponseDTO response = battleService.war(2L, requestDTO, attackingKingdom);
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
    Army attackingArmy = new Army();
    List<TroopEntity> troops = TroopFactory.createDefaultTroops();
    attackingArmy.setTroops(troops);
    int distance = 10;

    int hp = battleService.calculateHPforAttackingArmy(attackingArmy,distance);

    Assert.assertEquals(245, hp);
  }

  @Test
  public void calculateHPforAttackingArmy_ArmyNotSurvivesTravel_returns0hp() {
    Army attackingArmy = new Army();
    List<TroopEntity> troops = TroopFactory.createDefaultTroops();
    attackingArmy.setTroops(troops);
    KingdomEntity kingdom = KingdomFactory.createFullKingdom(1L,1L);
    kingdom.setTroops(troops);
    attackingArmy.setKingdom(kingdom);
    int distance = 1000;

    int hp = battleService.calculateHPforAttackingArmy(attackingArmy,distance);

    Assert.assertEquals(0, hp);
    Assert.assertEquals(0,attackingArmy.getTroops().size());
  }

  @Test
  public void killAllTroopsInArmy_returnsCorrectIDsOfKilledTroops_AndCorrecArmyAndKingdomTroopSizes() {
    Army army = new Army();
    army.setTroops(TroopFactory.createDefaultTroops());
    KingdomEntity kingdom = KingdomFactory.createFullKingdom(1L,1L);
    army.setKingdom(kingdom);
    //adding 1 extra troop which will not die in battle
    List<TroopEntity> troops = new ArrayList<>();
    troops.addAll(army.getTroops());
    troops.add(new TroopEntity(10L,1,100,100,100,100,100));
    army.getKingdom().setTroops(troops);
    List<Long> deadTroops = new ArrayList<>(Arrays.asList(1L,2L,3L));

    List<Long> ids = battleService.killAllTroopsInArmy(army);

    Assert.assertEquals(deadTroops, ids);
    Assert.assertEquals(0,army.getTroops().size());
    Assert.assertEquals(1,army.getKingdom().getTroops().size());
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
  public void prepareAttackingArmy_returnsAttackingArmy() {
    List<TroopEntity> attackingTroops = TroopFactory.createDefaultTroops();
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L, 1L);
    attackingKingdom.setTroops(attackingTroops);
    int distance = 10;

    Army army = battleService.prepareAttackingArmy(attackingTroops, attackingKingdom, distance);

    Assert.assertEquals(attackingKingdom, army.getKingdom());
    Assert.assertEquals(3, army.getKingdom().getTroops().size());
    Assert.assertEquals(3, army.getTroops().size());
    Assert.assertEquals(245, army.getHealthPoints());
    Assert.assertEquals(306, army.getAttackPoints());
    Assert.assertEquals(306, army.getDefencePoints());
    Assert.assertEquals(ArmyType.ATTACKINGARMY, army.getArmyType());
  }

  @Test
  public void prepareAttackingArmy_TroopsDidNotSurviveTravel() {
    List<TroopEntity> attackingTroops = TroopFactory.createDefaultTroops();
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L, 1L);
    attackingKingdom.setTroops(attackingTroops);
    int distance = 1000;

    Army army = battleService.prepareAttackingArmy(attackingTroops, attackingKingdom, distance);

    Assert.assertEquals(attackingKingdom, army.getKingdom());
    Assert.assertEquals(0, army.getKingdom().getTroops().size());
    Assert.assertEquals(0, army.getTroops().size());
    Assert.assertEquals(0, army.getHealthPoints());
    Assert.assertEquals(306, army.getAttackPoints());
    Assert.assertEquals(306, army.getDefencePoints());
    Assert.assertEquals(ArmyType.ATTACKINGARMY, army.getArmyType());
  }

  @Test
  public void getDefendingTroops_ReturnsCorrectSizeTroopsList() {
    KingdomEntity defendingKingdom = KingdomFactory.createFullKingdom(1L,1L);
    List<TroopEntity> troops = TroopFactory.createDefaultTroops(); //3 troops with ids 1-3
    defendingKingdom.setTroops(troops);

    List<TroopEntity> army = battleService.getDefendingTroops(defendingKingdom);

    Assert.assertEquals(3, army.size());
  }

  @Test
  public void calculateHPforDefendingArmy_ReturnsCorrectHealthPoints() {
    List<TroopEntity> troops = TroopFactory.createDefaultTroops(); //3 troops with ids 1-3

    int hp = battleService.calculateHPforDefendingArmy(troops);

    Assert.assertEquals(306, hp);
  }

  @Test
  public void calculateHPforDefendingArmy_NoTroopsHome_Returns0HealthPoints() {
    List<TroopEntity> troops = new ArrayList<>(); //empty list = no troop at home

    int hp = battleService.calculateHPforDefendingArmy(troops);

    Assert.assertEquals(0, hp);
  }

  @Test
  public void calculateDPforDefendingArmy_returnsCorrectDP() {
    KingdomEntity kingdom = KingdomFactory.createFullKingdom(1L,1L);
    Army army = new Army();
    army.setKingdom(kingdom);
    army.setTroops(kingdom.getTroops()); //2 troops, each 100DP

    Mockito.doReturn(0.03).when(battleService).calculateDefenceBonusCoeficient(kingdom);

    int defencePoints = battleService.calculateDPForDefendingArmy(army);

    Assert.assertEquals(206, defencePoints);
  }

  @Test
  public void calculateDefenceBonusCoeficient_Level1TownhallAndAcademy_returnsCorrectCoeficient() {
    KingdomEntity kingdom = KingdomFactory.createFullKingdom(1L,1L);
    BuildingEntity townhall = new BuildingEntity(kingdom, BuildingType.TOWNHALL,1);
    BuildingEntity academy = new BuildingEntity(kingdom, BuildingType.ACADEMY,1);

    Mockito.when(buildingService.findBuildingWithHighestLevel(kingdom,BuildingType.TOWNHALL))
        .thenReturn(townhall);
    Mockito.when(buildingService.findBuildingWithHighestLevel(kingdom,BuildingType.ACADEMY))
        .thenReturn(academy);

    double coef = battleService.calculateDefenceBonusCoeficient(kingdom);

    Assert.assertEquals(0.03, coef, 0);
  }

  @Test
  public void prepareDefendingArmy_Level1TownhallAndAcademy_returnsDefendingArmy() {
    KingdomEntity defendingKingdom = KingdomFactory.createFullKingdom(1L, 1L);
    BuildingEntity townhall = new BuildingEntity(defendingKingdom, BuildingType.TOWNHALL,1);
    BuildingEntity academy = new BuildingEntity(defendingKingdom, BuildingType.ACADEMY,1);

    Mockito.when(buildingService.findBuildingWithHighestLevel(defendingKingdom,BuildingType.TOWNHALL))
        .thenReturn(townhall);
    Mockito.when(buildingService.findBuildingWithHighestLevel(defendingKingdom,BuildingType.ACADEMY))
        .thenReturn(academy);

    Army army = battleService.prepareDefendingArmy(defendingKingdom);

    Assert.assertEquals(defendingKingdom, army.getKingdom());
    Assert.assertEquals(2, army.getKingdom().getTroops().size());
    Assert.assertEquals(2, army.getTroops().size());
    Assert.assertEquals(200, army.getHealthPoints());
    Assert.assertEquals(200, army.getAttackPoints());
    Assert.assertEquals(206, army.getDefencePoints());
    Assert.assertEquals(ArmyType.DEFENDINGARMY, army.getArmyType());
  }

  //Mark's tests
  @Test
  public void performAfterBattleActions_ShouldReturn_AttackingKingdomWonMessage() {
    List<Army> armies = ArmyFactory.createListOf2ArmiesWithProperTroops();
    int distance = 10;
    armies.get(1).setTroops(null);
    armies.get(0).setTroops(TroopFactory.createTroopsWithLowHp());

    Mockito.doReturn(armies.get(0)).when(battleService).getArmyByType(armies, ArmyType.ATTACKINGARMY);
    Mockito.doReturn(armies.get(1)).when(battleService).getArmyByType(armies, ArmyType.DEFENDINGARMY);
    Mockito.doReturn(null).when(battleService).nobodyOrDefKingdomWon(armies.get(1), armies.get(0));


    BattleResultDTO resultDTO = battleService.performAfterBattleActions(armies, distance);

    Assert.assertEquals("Defending Kingdom won", resultDTO.getWinningTeam());
    Assert.assertEquals(java.util.Optional.of(20), java.util.Optional.ofNullable(armies.get(1).getTroops().get(0).getHp()));
    Assert.assertEquals(java.util.Optional.of(40), java.util.Optional.ofNullable(armies.get(1).getTroops().get(1).getHp()));
    Assert.assertEquals(java.util.Optional.of(60), java.util.Optional.ofNullable(armies.get(1).getTroops().get(2).getHp()));

  }

  @Test
  public void performAfterBattleActions_ShouldReturn_JustStolenThings_BecauseNobodyWon() {

  }

  @Test
  public void nobodyOrDefKingdomWon_ShouldReturn_EveryTroopsDeadMessage() {
    List<Army> armies = ArmyFactory.createListOf2ArmiesWithProperTroops();
    armies.get(0).setTroops(null);
    armies.get(1).setTroops(null);

    BattleResultDTO resultDTO = battleService.nobodyOrDefKingdomWon(armies.get(1), armies.get(0));

    Assert.assertEquals("Every Troops were dead", resultDTO.getWinningTeam());
  }

  @Test
  public void nobodyOrDefKingdomWon_ShouldReturn_DefendingKingdomWonMessage() {
    List<Army> armies = ArmyFactory.createListOf2ArmiesWithProperTroops();
    armies.get(0).setTroops(null);
    armies.get(1).setTroops(TroopFactory.createTroopsWithLowHp());

    BattleResultDTO resultDTO = battleService.nobodyOrDefKingdomWon(armies.get(1), armies.get(0));

    Assert.assertEquals("Defending Kingdom won", resultDTO.getWinningTeam());
    Assert.assertEquals(java.util.Optional.of(20), java.util.Optional.ofNullable(armies.get(1).getTroops().get(0).getHp()));
    Assert.assertEquals(java.util.Optional.of(40), java.util.Optional.ofNullable(armies.get(1).getTroops().get(1).getHp()));
    Assert.assertEquals(java.util.Optional.of(60), java.util.Optional.ofNullable(armies.get(1).getTroops().get(2).getHp()));
  }

  @Test
  public void calculateStolenResource_ShouldReturn_ProperValues() {
    KingdomEntity kingdom = KingdomFactory.createKingdomEntityWithId(1L);
    Army attackingArmy = ArmyFactory.createAttackingArmyWithProperTroops();
    Army defendingArmy = ArmyFactory.createDefendingArmyWithProperTroops();
    ResourceEntity stolenFood = ResourceFactory.createResourcesWithAllDataAndHighAMount(kingdom).get(1);
    ResourceEntity gold = ResourceFactory.createResourcesWithAllDataAndHighAMount(kingdom).get(0);

    Mockito.when(resourceService.getResourceByResourceType(defendingArmy.getKingdom(), ResourceType.FOOD)).thenReturn(stolenFood);
    Mockito.when(resourceService.getResourceByResourceType(defendingArmy.getKingdom(), ResourceType.GOLD)).thenReturn(gold);

    //in parameters, the first ResourceType is that what we would like the steal now
    int result = battleService.calculateStolenResource(defendingArmy, attackingArmy, ResourceType.FOOD, ResourceType.GOLD);

    Assert.assertEquals(60 , result);
  }

  @Test
  public void calculateStolenResource_ShouldReturn_ProperValuesIfFoodAmountIsLessThanGoldAmount() {
    KingdomEntity kingdom = KingdomFactory.createKingdomEntityWithId(1L);
    Army attackingArmy = ArmyFactory.createAttackingArmyWithProperTroops();
    Army defendingArmy = ArmyFactory.createDefendingArmyWithProperTroops();
    ResourceEntity stolenFood = ResourceFactory.createResourcesWithAllDataAndHighAMount(kingdom).get(1);
    ResourceEntity gold = ResourceFactory.createResourcesWithAllDataAndHighAMount(kingdom).get(0);
    stolenFood.setAmount(30);

    Mockito.when(resourceService.getResourceByResourceType(defendingArmy.getKingdom(), ResourceType.FOOD)).thenReturn(stolenFood);
    Mockito.when(resourceService.getResourceByResourceType(defendingArmy.getKingdom(), ResourceType.GOLD)).thenReturn(gold);

    //in parameters, the first ResourceType is that what we would like the steal now
    int result = battleService.calculateStolenResource(defendingArmy, attackingArmy, ResourceType.FOOD, ResourceType.GOLD);

    Assert.assertEquals(30 , result);
  }

  @Test
  public void calculateStolenResource_ShouldReturn_ProperValuesIfFoodAmountIsHigherThanGoldAmount() {
    KingdomEntity kingdom = KingdomFactory.createKingdomEntityWithId(1L);
    Army attackingArmy = ArmyFactory.createAttackingArmyWithProperTroops();
    Army defendingArmy = ArmyFactory.createDefendingArmyWithProperTroops();
    ResourceEntity stolenFood = ResourceFactory.createResourcesWithAllDataAndHighAMount(kingdom).get(1);
    ResourceEntity gold = ResourceFactory.createResourcesWithAllDataAndHighAMount(kingdom).get(0);
    gold.setAmount(30);

    Mockito.when(resourceService.getResourceByResourceType(defendingArmy.getKingdom(), ResourceType.FOOD)).thenReturn(stolenFood);
    Mockito.when(resourceService.getResourceByResourceType(defendingArmy.getKingdom(), ResourceType.GOLD)).thenReturn(gold);

    //in parameters, the first ResourceType is that what we would like the steal now
    int result = battleService.calculateStolenResource(defendingArmy, attackingArmy, ResourceType.FOOD, ResourceType.GOLD);

    Assert.assertEquals(90 , result);
  }

  @Test
  public void getArmyByType_ShouldReturn_CorrectArmy() {
    List<Army> armies = ArmyFactory.createListOf2Armies();

    Army result = battleService.getArmyByType(armies, ArmyType.ATTACKINGARMY);

    Assert.assertEquals(armies.get(0), result);
    Assert.assertEquals(ArmyType.ATTACKINGARMY, result.getArmyType());
  }

  @Test
  public void defineTroopHp_ShouldReturn_CorrectValue() {
    Integer result = battleService.defineTroopHp();

    Assert.assertEquals(java.util.Optional.of(20), java.util.Optional.ofNullable(result));
  }
}
