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
import com.greenfoxacademy.springwebapp.common.services.TimeService;
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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class BattleServiceTest {
  private KingdomService kingdomService;
  private BuildingService buildingService;
  private TroopService troopService;
  private ResourceService resourceService;
  private BattleServiceImpl battleService;
  private TimeService timeService;

  @Before
  public void init() {
    kingdomService = Mockito.mock(KingdomService.class);
    buildingService = Mockito.mock(BuildingService.class);
    troopService = Mockito.mock(TroopService.class);
    Environment env = TestConfig.mockEnvironment();
    resourceService = Mockito.mock(ResourceService.class);
    timeService = Mockito.mock(TimeService.class);
    battleService = new BattleServiceImpl(kingdomService, buildingService, troopService,
        env, resourceService, timeService);
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
    Mockito.doReturn(1).when(battleService).scheduleBattle(attackingKingdom,troops, defendingKingdom);

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

  //"Prepare attacking army" section


  @Test
  public void prepareAttackingArmy_returnsAttackingArmy() {
    //note: its a kind of "integration" unit test
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
    //note: its a kind of "integration" unit test
    List<TroopEntity> attackingTroops = TroopFactory.createDefaultTroops();
    KingdomEntity attackingKingdom = KingdomFactory.createFullKingdom(1L, 1L);
    attackingKingdom.setTroops(attackingTroops);
    int distance = 50; //each troop looses 2% of original HP each distance, so after 50 he is dead

    Army army = battleService.prepareAttackingArmy(attackingTroops, attackingKingdom, distance);

    Assert.assertEquals(attackingKingdom, army.getKingdom());
    Assert.assertEquals(0, army.getKingdom().getTroops().size());
    Assert.assertEquals(0, army.getTroops().size());
    Assert.assertEquals(0, army.getHealthPoints());
    Assert.assertEquals(0, army.getAttackPoints());
    Assert.assertEquals(0, army.getDefencePoints());
    Assert.assertEquals(ArmyType.ATTACKINGARMY, army.getArmyType());
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
    Army army = ArmyFactory.createAttackingArmy(1);
    int distance = 10;

    Mockito.doReturn(army).when(battleService).applyHpLossDueToTravelling(army,distance);

    int hp = battleService.calculateHPforAttackingArmy(army,distance);

    Assert.assertEquals(120, hp);
  }

  @Test
  public void calculateHPforAttackingArmy_ArmyWithNegativeHp_returns0hp() {
    Army army = ArmyFactory.createAttackingArmy(1);
    army.getTroops().get(0).setHp(-400); //therefore whole army has negative sum of HP
    int distance = 10;

    Mockito.doReturn(army).when(battleService).applyHpLossDueToTravelling(army,distance);

    int hp = battleService.calculateHPforAttackingArmy(army,distance);

    Assert.assertEquals(0, hp);
  }

  @Test
  public void applyHpLossDueToTravelling_nobodyDies_returnsCorrecArmyAndKingdomTroopSizes() {
    Army army = ArmyFactory.createAttackingArmy(1);
    army.setTroops(TroopFactory.createDefaultTroops());
    int distance = 10;

    Army updatedArmy = battleService.applyHpLossDueToTravelling(army, distance);

    Assert.assertEquals(3,updatedArmy.getTroops().size());
    Assert.assertEquals(3,updatedArmy.getKingdom().getTroops().size());
    Assert.assertEquals(81,updatedArmy.getTroops().get(0).getHp().intValue());
    Assert.assertEquals(82,updatedArmy.getTroops().get(1).getHp().intValue());
    Assert.assertEquals(82,updatedArmy.getTroops().get(2).getHp().intValue());
  }

  @Test
  public void applyHpLossDueToTravelling_everyoneDies_returnsCorrecArmyAndKingdomTroopSizes() {
    Army army = ArmyFactory.createAttackingArmy(1);
    int distance = 50; //since troops looses 2% per distance, every troop dies after 50 distance

    Army updatedArmy = battleService.applyHpLossDueToTravelling(army, distance);

    Assert.assertEquals(0,updatedArmy.getTroops().size());
    Assert.assertEquals(0,updatedArmy.getKingdom().getTroops().size());
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


  //"Prepare defending army" section

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

    int defencePoints = battleService.calculateDPforDefendingArmy(army);

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

  //"Fight Armies" section

  @Test
  public void fightArmies_returnsCorrectListOfArmies() {
    Army attackingArmy = ArmyFactory.createAttackingArmy(1);
    Army defendingArmy = ArmyFactory.createDefendingArmy(2);

    Mockito.doReturn(attackingArmy).when(battleService).fightOponent(any(),any());
    Mockito.doReturn(attackingArmy.getTroops()).when(battleService).removeDeadTroopsFromArmy(any());
    Mockito.doReturn(attackingArmy).when(battleService).removeDeadTroopsFromKingdom(any(),any());
    Mockito.doReturn(attackingArmy).when(battleService).updateArmyPointsAfterFight(any());

    List<Army> armiesAfterBattle = battleService.fightArmies(attackingArmy, defendingArmy);

    Assert.assertEquals(2,armiesAfterBattle.size());
    Assert.assertEquals(attackingArmy,armiesAfterBattle.get(0));
    Assert.assertEquals(defendingArmy,armiesAfterBattle.get(1));
  }

  @Test
  public void fightArmies_noOneDies_ArmiesDontHarmEachOther() {
    //note: its a kind of "integration" unit test
    Army attackingArmy = ArmyFactory.createAttackingArmy(1);
    attackingArmy.setTroops(TroopFactory.createDefaultTroops());
    Army defendingArmy = ArmyFactory.createDefendingArmy(2);
    defendingArmy.setTroops(TroopFactory.createDefaultTroops());

    List<Army> armiesAfterBattle = battleService.fightArmies(attackingArmy, defendingArmy);

    Assert.assertEquals(2,armiesAfterBattle.size());
    Assert.assertEquals(3,armiesAfterBattle.get(0).getTroops().size());
    Assert.assertEquals(3,armiesAfterBattle.get(1).getTroops().size());
    Assert.assertEquals(101,armiesAfterBattle.get(0).getTroops().get(0).getHp().intValue());
    Assert.assertEquals(101,armiesAfterBattle.get(1).getTroops().get(0).getHp().intValue());
  }

  @Test
  public void performAfterBattleActions_ShouldReturn_AttackingKingdomWonMessage() {
    List<Army> armies = ArmyFactory.createArmiesForBattle();
    armies.get(0).setTroops(TroopFactory.createTroopsWithLowHp());
    armies.get(1).setHealthPoints(0);

    mockingPart_PerformAfterBattleActions_WhenAttackingKingdomCanSteal(armies);
    int distance = 10;

    BattleResultDTO resultDTO = battleService.performAfterBattleActions(armies, distance);

    Assert.assertEquals("Attacking Kingdom won", resultDTO.getWinningTeam());
    Assert.assertEquals(50, resultDTO.getStolenFood());
    Assert.assertEquals(50, resultDTO.getStolenGold());
    //TODO: have to check that the deleteAllTroops is working well or not
  }

  @Test
  public void performAfterBattleActions_ShouldReturn_JustStolenThings_BecauseNobodyWon() {
    List<Army> armies = ArmyFactory.createArmiesForBattle();
    int distance = 10;
    armies.get(0).setTroops(TroopFactory.createTroopsWithLowHp());

    mockingPart_PerformAfterBattleActions_WhenAttackingKingdomCanSteal(armies);

    BattleResultDTO resultDTO = battleService.performAfterBattleActions(armies, distance);

    Assert.assertEquals("Attacking Kingdom won", resultDTO.getWinningTeam());
    Assert.assertEquals(50, resultDTO.getStolenFood());
    Assert.assertEquals(50, resultDTO.getStolenGold());
  }

  @Test
  public void performAfterBattleActions_ShouldReturn_JustStolenThings_AttackKingdomWon() {
    List<Army> armies = ArmyFactory.createArmiesForBattle();
    armies.get(0).setTroops(TroopFactory.createTroopsWithLowHp());
    armies.get(0).setHealthPoints(50);
    int distance = 10;

    mockingPart_PerformAfterBattleActions_WhenAttackingKingdomCanSteal_10_100(armies);

    BattleResultDTO resultDTO = battleService.performAfterBattleActions(armies, distance);

    Assert.assertEquals("Attacking Kingdom won", resultDTO.getWinningTeam());
    Assert.assertEquals(10, resultDTO.getStolenFood());
    Assert.assertEquals(40, resultDTO.getStolenGold());
  }

  @Test
  public void nobodyWon_ShouldReturn_True() {
    List<Army> armies = ArmyFactory.createArmiesForBattle();
    armies.get(0).setHealthPoints(0);   //0 = Attacking Troops
    armies.get(1).setHealthPoints(0);   //1 = Defending Troops

    boolean result = battleService.nobodyWon(armies.get(1), armies.get(0));

    Assert.assertTrue(result);
  }

  @Test
  public void nobodyWon_ShouldReturn_False_IfAttackingTroopsHasHp() {
    List<Army> armies = ArmyFactory.createArmiesForBattle();
    armies.get(0).setHealthPoints(10);
    armies.get(1).setHealthPoints(0);

    boolean result = battleService.nobodyWon(armies.get(1), armies.get(0));

    Assert.assertFalse(result);
  }

  @Test
  public void nobodyWon_ShouldReturn_False_IfDefendingTroopsHasHp() {
    List<Army> armies = ArmyFactory.createArmiesForBattle();
    armies.get(0).setHealthPoints(0);
    armies.get(1).setHealthPoints(10);

    boolean result = battleService.nobodyWon(armies.get(1), armies.get(0));

    Assert.assertFalse(result);
  }

  @Test
  public void defKingdomWon_ShouldReturn_True() {
    List<Army> armies = ArmyFactory.createArmiesForBattle();
    armies.get(0).setHealthPoints(0);
    armies.get(1).setTroops(TroopFactory.createTroopsWithLowHp());

    boolean result = battleService.defKingdomWon(armies.get(1), armies.get(0));

    Assert.assertTrue(result);
  }

  @Test
  public void defKingdomWon_ShouldReturn_False_IfAttackingTroopsHasHp() {
    List<Army> armies = ArmyFactory.createArmiesForBattle();
    armies.get(0).setTroops(TroopFactory.createTroopsWithLowHp());
    armies.get(1).setTroops(TroopFactory.createTroopsWithLowHp());

    boolean result = battleService.defKingdomWon(armies.get(1), armies.get(0));

    Assert.assertFalse(result);
  }

  @Test
  public void fightArmies_oneTroopSurvives_returnsCorrectListOfArmiesAndCorrectSizesOfTroopLists() {
    //note: its a kind of "integration" unit test
    Army attackingArmy = ArmyFactory.createAttackingArmy(1);
    attackingArmy.getTroops().get(0).setAttack(300);
    attackingArmy.getTroops().get(0).setDefence(150); //only this troop with id=1 shall survive
    Army defendingArmy = ArmyFactory.createDefendingArmy(2);
    defendingArmy.getTroops().get(0).setAttack(300);

    List<Army> armiesAfterBattle = battleService.fightArmies(attackingArmy, defendingArmy);

    Assert.assertEquals(2,armiesAfterBattle.size());
    Assert.assertEquals(1,armiesAfterBattle.get(0).getTroops().size());
    Assert.assertEquals(0,armiesAfterBattle.get(1).getTroops().size());
    //expected id of survived troop
    Assert.assertEquals(1,armiesAfterBattle.get(0).getTroops().get(0).getId().intValue());
  }

  @Test
  public void fightArmies_allTroopsDie_returnsCorrectListOfArmiesAndZeroSizeOfTroopLists() {
    //note: its a kind of "integration" unit test
    Army attackingArmy = ArmyFactory.createAttackingArmy(1);
    attackingArmy.getTroops().get(0).setAttack(300);
    Army defendingArmy = ArmyFactory.createDefendingArmy(1);
    defendingArmy.getTroops().get(0).setAttack(300);

    List<Army> armiesAfterBattle = battleService.fightArmies(attackingArmy, defendingArmy);

    Assert.assertEquals(2,armiesAfterBattle.size());
    Assert.assertEquals(0,armiesAfterBattle.get(0).getTroops().size());
    Assert.assertEquals(0,armiesAfterBattle.get(1).getTroops().size());
  }

  @Test
  public void fightOpponent_returnsArmyWithUpdatedTroops() {
    Army army1 = ArmyFactory.createAttackingArmy(1);
    Army army2 = ArmyFactory.createDefendingArmy(1);
    List<TroopEntity> damagedTroops = TroopFactory.createDefaultTroops();
    int damage = 50;

    Mockito.doReturn(damage).when(battleService).calculateIncuredDamage(army1,army2);
    Mockito.doReturn(damagedTroops).when(battleService).shareDamageAmongTroops(army1,damage);

    Army updatedArmy = battleService.fightOponent(army1,army2);

    Assert.assertEquals(damagedTroops.size(),updatedArmy.getTroops().size());
    Assert.assertEquals(damagedTroops.get(0).getId(),updatedArmy.getTroops().get(0).getId());
  }

  @Test
  public void calculateIncuredDamage_shouldReturnCorrectDamage() {
    Army army1 = ArmyFactory.createAttackingArmy(1);
    army1.getTroops().get(0).setDefence(1);
    Army army2 = ArmyFactory.createDefendingArmy(2);

    int damage = battleService.calculateIncuredDamage(army1,army2);

    Assert.assertEquals(34, damage);
  }

  @Test
  public void calculateIncuredDamage_shouldReturnZeroDamage() {
    Army army1 = ArmyFactory.createAttackingArmy(1);
    Army army2 = ArmyFactory.createDefendingArmy(2);
    army2.getTroops().get(2).setAttack(0);

    int damage = battleService.calculateIncuredDamage(army1,army2);

    Assert.assertEquals(0, damage);
  }

  @Test
  public void shareDamageAmongTroops_returnsDamagedTroops() {
    Army army = ArmyFactory.createAttackingArmy(1);
    army.setTroops(TroopFactory.createDefaultTroops());
    final int incuredDamage = 100;
    army.getTroops().get(0).setDefence(300);

    List<TroopEntity> damagedTroops = battleService.shareDamageAmongTroops(army,incuredDamage);

    Assert.assertEquals(3,damagedTroops.size());
    Assert.assertEquals(86,damagedTroops.get(0).getHp().intValue());
    Assert.assertEquals(59,damagedTroops.get(1).getHp().intValue());
    Assert.assertEquals(61,damagedTroops.get(2).getHp().intValue());
  }

  @Test
  public void removeDeadTroopsFromArmy_returnsOnlySurvivedTroops() {
    Army army = ArmyFactory.createAttackingArmy(1);
    army.getTroops().get(0).setHp(0);

    List<TroopEntity> aliveTroops = battleService.removeDeadTroopsFromArmy(army);

    Assert.assertEquals(2,aliveTroops.size());
  }

  @Test
  public void removeDeadTroopsFromKingdom_returnsUpdtedArmy() {
    Army army = ArmyFactory.createAttackingArmy(1);
    List<TroopEntity> originalListOfTroops = new ArrayList<>();
    originalListOfTroops.addAll(army.getTroops());
    army.getTroops().remove(0); //removing one troop from fighting troops (he died)

    Army updatedArmy = battleService.removeDeadTroopsFromKingdom(army,originalListOfTroops);

    Assert.assertEquals(2,updatedArmy.getKingdom().getTroops().size());
  }

  @Test
  public void updateArmyPointsAfterFight_returnsUpdtedArmy() {
    Army army = ArmyFactory.createAttackingArmy(1);
    army.setTroops(TroopFactory.createDefaultTroops());
    army.setHealthPoints(10);
    army.setDefencePoints(10);
    army.setAttackPoints(10);

    Army updatedArmy = battleService.updateArmyPointsAfterFight(army);

    Assert.assertEquals(306,updatedArmy.getHealthPoints());
    Assert.assertEquals(306,updatedArmy.getDefencePoints());
    Assert.assertEquals(306,updatedArmy.getAttackPoints());
  }

  @Test
  public void calculateStolenResource_ShouldReturn_50_IfStolenFood1000AndGold1000() {
    Army attackingArmy = ArmyFactory.createAttackingArmy(1);
    Army defendingArmy = ArmyFactory.createDefendingArmy(2);
    ResourceEntity stolenFood = ResourceFactory.createResourcesWithAllDataWithHighAmount().get(1);
    ResourceEntity gold = ResourceFactory.createResourcesWithAllDataWithHighAmount().get(0);

    Mockito.when(resourceService.calculateActualResource(defendingArmy.getKingdom(), ResourceType.FOOD))
        .thenReturn(stolenFood.getAmount());
    Mockito.when(resourceService.calculateActualResource(defendingArmy.getKingdom(), ResourceType.GOLD))
        .thenReturn(gold.getAmount());

    //in parameters, the first ResourceType is that what we would like the steal
    int result =
        battleService.calculateStolenResource(defendingArmy, attackingArmy, ResourceType.FOOD, ResourceType.GOLD);

    Assert.assertEquals(50, result);
  }

  @Test
  public void calculateStolenResource_ShouldReturn_30_IfStolenFood30AndGold1000() {
    Army defendingArmy = ArmyFactory.createDefendingArmy(2);
    ResourceEntity stolenFood = ResourceFactory.createResourcesWithAllDataWithHighAmount().get(1);
    ResourceEntity gold = ResourceFactory.createResourcesWithAllDataWithHighAmount().get(0);
    stolenFood.setAmount(30);
    Army attackingArmy = ArmyFactory.createAttackingArmy(1);

    Mockito.when(resourceService.calculateActualResource(defendingArmy.getKingdom(), ResourceType.FOOD))
        .thenReturn(stolenFood.getAmount());
    Mockito.when(resourceService.calculateActualResource(defendingArmy.getKingdom(), ResourceType.GOLD))
        .thenReturn(gold.getAmount());

    //in parameters, the first ResourceType is that what we would like the steal
    int result =
        battleService.calculateStolenResource(defendingArmy, attackingArmy, ResourceType.FOOD, ResourceType.GOLD);

    Assert.assertEquals(30, result);
  }

  @Test
  public void calculateStolenResource_ShouldReturn_70_IfStolenFood1000AndGold30() {
    Army defendingArmy = ArmyFactory.createDefendingArmy(2);
    ResourceEntity stolenFood = ResourceFactory.createResourcesWithAllDataWithHighAmount().get(1);
    ResourceEntity gold = ResourceFactory.createResourcesWithAllDataWithHighAmount().get(0);
    gold.setAmount(30);
    Army attackingArmy = ArmyFactory.createAttackingArmy(1);

    Mockito.when(resourceService.calculateActualResource(defendingArmy.getKingdom(), ResourceType.FOOD))
        .thenReturn(stolenFood.getAmount());
    Mockito.when(resourceService.calculateActualResource(defendingArmy.getKingdom(), ResourceType.GOLD))
        .thenReturn(gold.getAmount());

    //in parameters, the first ResourceType is that what we would like the steal
    int result =
        battleService.calculateStolenResource(defendingArmy, attackingArmy, ResourceType.FOOD, ResourceType.GOLD);

    Assert.assertEquals(70, result);
  }

  @Test
  public void calculateStolenResource_ShouldReturn_60_IfStolenFood60AndGold10() {
    Army defendingArmy = ArmyFactory.createDefendingArmy(2);
    ResourceEntity stolenFood = ResourceFactory.createResourcesWithAllDataWithHighAmount().get(1);
    ResourceEntity gold = ResourceFactory.createResourcesWithAllDataWithHighAmount().get(0);
    stolenFood.setAmount(60);
    gold.setAmount(10);
    Army attackingArmy = ArmyFactory.createAttackingArmy(1);

    Mockito.when(resourceService.calculateActualResource(defendingArmy.getKingdom(), ResourceType.FOOD))
        .thenReturn(stolenFood.getAmount());
    Mockito.when(resourceService.calculateActualResource(defendingArmy.getKingdom(), ResourceType.GOLD))
        .thenReturn(gold.getAmount());

    //in parameters, the first ResourceType is that what we would like the steal
    int result =
        battleService.calculateStolenResource(defendingArmy, attackingArmy, ResourceType.FOOD, ResourceType.GOLD);

    Assert.assertEquals(60, result);
  }

  @Test
  public void calculateStolenResource_ShouldReturn_20_IfStolenFood20AndGold20() {
    Army defendingArmy = ArmyFactory.createDefendingArmy(2);
    ResourceEntity stolenFood = ResourceFactory.createResourcesWithAllDataWithHighAmount().get(1);
    ResourceEntity gold = ResourceFactory.createResourcesWithAllDataWithHighAmount().get(0);
    stolenFood.setAmount(20);
    gold.setAmount(20);
    Army attackingArmy = ArmyFactory.createAttackingArmy(1);

    Mockito.when(resourceService.calculateActualResource(defendingArmy.getKingdom(), ResourceType.FOOD))
        .thenReturn(stolenFood.getAmount());
    Mockito.when(resourceService.calculateActualResource(defendingArmy.getKingdom(), ResourceType.GOLD))
        .thenReturn(gold.getAmount());

    //in parameters, the first ResourceType is that what we would like the steal
    int result =
        battleService.calculateStolenResource(defendingArmy, attackingArmy, ResourceType.FOOD, ResourceType.GOLD);

    Assert.assertEquals(20, result);
  }

  @Test
  public void calculateStolenResource_ShouldReturn_20_IfStolenFood20AndGold70() {
    Army defendingArmy = ArmyFactory.createDefendingArmy(2);
    ResourceEntity stolenFood = ResourceFactory.createResourcesWithAllDataWithHighAmount().get(1);
    ResourceEntity gold = ResourceFactory.createResourcesWithAllDataWithHighAmount().get(0);
    stolenFood.setAmount(20);
    gold.setAmount(70);
    Army attackingArmy = ArmyFactory.createAttackingArmy(1);

    Mockito.when(resourceService.calculateActualResource(defendingArmy.getKingdom(), ResourceType.FOOD))
        .thenReturn(stolenFood.getAmount());
    Mockito.when(resourceService.calculateActualResource(defendingArmy.getKingdom(), ResourceType.GOLD))
        .thenReturn(gold.getAmount());

    //in parameters, the first ResourceType is that what we would like the steal
    int result =
        battleService.calculateStolenResource(defendingArmy, attackingArmy, ResourceType.FOOD, ResourceType.GOLD);

    Assert.assertEquals(20, result);
  }

  @Test
  public void calculateStolenResource_ShouldReturn_50_IfStolenFood80AndGold80() {
    Army defendingArmy = ArmyFactory.createDefendingArmy(2);
    ResourceEntity stolenFood = ResourceFactory.createResourcesWithAllDataWithHighAmount().get(1);
    ResourceEntity gold = ResourceFactory.createResourcesWithAllDataWithHighAmount().get(0);
    stolenFood.setAmount(80);
    gold.setAmount(80);
    Army attackingArmy = ArmyFactory.createAttackingArmy(1);

    Mockito.when(resourceService.calculateActualResource(defendingArmy.getKingdom(), ResourceType.FOOD))
        .thenReturn(stolenFood.getAmount());
    Mockito.when(resourceService.calculateActualResource(defendingArmy.getKingdom(), ResourceType.GOLD))
        .thenReturn(gold.getAmount());

    //in parameters, the first ResourceType is that what we would like the steal
    int result =
        battleService.calculateStolenResource(defendingArmy, attackingArmy, ResourceType.FOOD, ResourceType.GOLD);

    Assert.assertEquals(50, result);
  }

  @Test
  public void calculateStolenResource_ShouldReturn_50_IfStolenFood80AndGold80afasfasfa() {
    ResourceEntity stolenFood = ResourceFactory.createResourcesWithAllDataWithHighAmount().get(1);
    ResourceEntity gold = ResourceFactory.createResourcesWithAllDataWithHighAmount().get(0);
    stolenFood.setAmount(100);
    gold.setAmount(10);
    Army attackingArmy = ArmyFactory.createAttackingArmy(1);
    Army defendingArmy = ArmyFactory.createDefendingArmy(2);
    attackingArmy.setHealthPoints(50);

    Mockito.when(resourceService.calculateActualResource(defendingArmy.getKingdom(), ResourceType.FOOD))
        .thenReturn(stolenFood.getAmount());
    Mockito.when(resourceService.calculateActualResource(defendingArmy.getKingdom(), ResourceType.GOLD))
        .thenReturn(gold.getAmount());

    //in parameters, the first ResourceType is that what we would like the steal
    int result =
        battleService.calculateStolenResource(defendingArmy, attackingArmy, ResourceType.FOOD, ResourceType.GOLD);

    Assert.assertEquals(40, result);
  }

  @Test
  public void calculateStolenResource_ShouldReturn_50_IfStolenFood80AndGold80afafasa() {
    ResourceEntity stolenFood = ResourceFactory.createResourcesWithAllDataWithHighAmount().get(1);
    ResourceEntity gold = ResourceFactory.createResourcesWithAllDataWithHighAmount().get(0);
    stolenFood.setAmount(10);
    gold.setAmount(100);
    Army attackingArmy = ArmyFactory.createAttackingArmy(1);
    Army defendingArmy = ArmyFactory.createDefendingArmy(2);
    attackingArmy.setHealthPoints(50);

    Mockito.when(resourceService.calculateActualResource(defendingArmy.getKingdom(), ResourceType.FOOD))
        .thenReturn(stolenFood.getAmount());
    Mockito.when(resourceService.calculateActualResource(defendingArmy.getKingdom(), ResourceType.GOLD))
        .thenReturn(gold.getAmount());

    //in parameters, the first ResourceType is that what we would like the steal
    int result =
        battleService.calculateStolenResource(defendingArmy, attackingArmy, ResourceType.FOOD, ResourceType.GOLD);

    Assert.assertEquals(10, result);
  }

  @Test
  public void getArmyByType_ShouldReturn_CorrectArmy() {
    List<Army> armies = ArmyFactory.createArmiesForBattle();

    Army result = battleService.getArmyByType(armies, ArmyType.ATTACKINGARMY);

    Assert.assertEquals(armies.get(0), result);
    Assert.assertEquals(ArmyType.ATTACKINGARMY, result.getArmyType());
  }

  @Test
  public void defineTroopHp_ShouldReturn_CorrectValue() {
    Integer result = battleService.defineTroopHp();

    Assert.assertEquals(java.util.Optional.of(20), java.util.Optional.ofNullable(result));
  }

  public void mockingPart_PerformAfterBattleActions_WhenAttackingKingdomCanSteal(List<Army> armies) {
    Mockito.doReturn(armies.get(0)).when(battleService).getArmyByType(armies, ArmyType.ATTACKINGARMY);
    Mockito.doReturn(armies.get(1)).when(battleService).getArmyByType(armies, ArmyType.DEFENDINGARMY);
    Mockito.doReturn(false).when(battleService).nobodyWon(armies.get(1), armies.get(0));
    Mockito.doReturn(false).when(battleService).defKingdomWon(armies.get(1), armies.get(0));
    Mockito.doReturn(50).when(battleService)
        .calculateStolenResource(armies.get(1), armies.get(0), ResourceType.FOOD, ResourceType.GOLD);
    Mockito.doReturn(50).when(battleService)
        .calculateStolenResource(armies.get(1), armies.get(0), ResourceType.GOLD, ResourceType.FOOD);
    Mockito.when(resourceService.getResourceByResourceType(armies.get(1).getKingdom(), ResourceType.GOLD))
        .thenReturn(armies.get(1).getKingdom().getResources().get(0));
    Mockito.when(resourceService.getResourceByResourceType(armies.get(1).getKingdom(), ResourceType.FOOD))
        .thenReturn(armies.get(1).getKingdom().getResources().get(1));
    Mockito.when(resourceService.getResourceByResourceType(armies.get(0).getKingdom(), ResourceType.GOLD))
        .thenReturn(armies.get(1).getKingdom().getResources().get(0));
    Mockito.when(resourceService.getResourceByResourceType(armies.get(0).getKingdom(), ResourceType.FOOD))
        .thenReturn(armies.get(1).getKingdom().getResources().get(1));
  }

  public void mockingPart_PerformAfterBattleActions_WhenAttackingKingdomCanSteal_10_100(List<Army> armies) {
    Mockito.doReturn(armies.get(0)).when(battleService).getArmyByType(armies, ArmyType.ATTACKINGARMY);
    Mockito.doReturn(armies.get(1)).when(battleService).getArmyByType(armies, ArmyType.DEFENDINGARMY);
    Mockito.doReturn(false).when(battleService).nobodyWon(armies.get(1), armies.get(0));
    Mockito.doReturn(false).when(battleService).defKingdomWon(armies.get(1), armies.get(0));
    Mockito.doReturn(10).when(battleService)
        .calculateStolenResource(armies.get(1), armies.get(0), ResourceType.FOOD, ResourceType.GOLD);
    Mockito.doReturn(40).when(battleService)
        .calculateStolenResource(armies.get(1), armies.get(0), ResourceType.GOLD, ResourceType.FOOD);
    Mockito.when(resourceService.getResourceByResourceType(armies.get(1).getKingdom(), ResourceType.GOLD))
        .thenReturn(armies.get(1).getKingdom().getResources().get(0));
    Mockito.when(resourceService.getResourceByResourceType(armies.get(1).getKingdom(), ResourceType.FOOD))
        .thenReturn(armies.get(1).getKingdom().getResources().get(1));
    Mockito.when(resourceService.getResourceByResourceType(armies.get(0).getKingdom(), ResourceType.GOLD))
        .thenReturn(armies.get(1).getKingdom().getResources().get(0));
    Mockito.when(resourceService.getResourceByResourceType(armies.get(0).getKingdom(), ResourceType.FOOD))
        .thenReturn(armies.get(1).getKingdom().getResources().get(1));
  }
}
