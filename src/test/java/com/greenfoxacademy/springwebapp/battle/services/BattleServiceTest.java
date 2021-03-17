package com.greenfoxacademy.springwebapp.battle.services;


import static org.mockito.ArgumentMatchers.any;


import com.greenfoxacademy.springwebapp.battle.models.Army;
import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleRequestDTO;
import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleResponseDTO;
import com.greenfoxacademy.springwebapp.battle.models.enums.ArmyType;
import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.factories.ArmyFactory;
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
    Army army = ArmyFactory.createAttackingArmy();
    int distance = 10;

    Mockito.doReturn(army).when(battleService).applyHpLossDueToTravelling(army,distance);

    int hp = battleService.calculateHPforAttackingArmy(army,distance);

    Assert.assertEquals(306, hp);
  }

  @Test
  public void calculateHPforAttackingArmy_ArmyWithNegativeHp_returns0hp() {
    Army army = ArmyFactory.createAttackingArmy();
    army.getTroops().get(0).setHp(-400); //therefore whole army has negative sum of HP
    int distance = 10;

    Mockito.doReturn(army).when(battleService).applyHpLossDueToTravelling(army,distance);

    int hp = battleService.calculateHPforAttackingArmy(army,distance);

    Assert.assertEquals(0, hp);
  }

  @Test
  public void applyHpLossDueToTravelling_nobodyDies_returnsCorrecArmyAndKingdomTroopSizes() {
    Army army = ArmyFactory.createAttackingArmy();
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
    Army army = ArmyFactory.createAttackingArmy();
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
    Army attackingArmy = ArmyFactory.createAttackingArmy();
    Army defendingArmy = ArmyFactory.createDefendingArmy();

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
    Army attackingArmy = ArmyFactory.createAttackingArmy();
    Army defendingArmy = ArmyFactory.createDefendingArmy();

    List<Army> armiesAfterBattle = battleService.fightArmies(attackingArmy, defendingArmy);

    Assert.assertEquals(2,armiesAfterBattle.size());
    Assert.assertEquals(3,armiesAfterBattle.get(0).getTroops().size());
    Assert.assertEquals(3,armiesAfterBattle.get(1).getTroops().size());
    Assert.assertEquals(101,armiesAfterBattle.get(0).getTroops().get(0).getHp().intValue());
    Assert.assertEquals(101,armiesAfterBattle.get(1).getTroops().get(0).getHp().intValue());
  }

  @Test
  public void fightArmies_oneTroopSurvives_returnsCorrectListOfArmiesAndCorrectSizesOfTroopLists() {
    //note: its a kind of "integration" unit test
    Army attackingArmy = ArmyFactory.createAttackingArmy();
    attackingArmy.getTroops().get(0).setAttack(300);
    attackingArmy.getTroops().get(0).setDefence(150); //only this troop with id=1 shall survive
    Army defendingArmy = ArmyFactory.createDefendingArmy();
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
    Army attackingArmy = ArmyFactory.createAttackingArmy();
    attackingArmy.getTroops().get(0).setAttack(300);
    Army defendingArmy = ArmyFactory.createDefendingArmy();
    defendingArmy.getTroops().get(0).setAttack(300);

    List<Army> armiesAfterBattle = battleService.fightArmies(attackingArmy, defendingArmy);

    Assert.assertEquals(2,armiesAfterBattle.size());
    Assert.assertEquals(0,armiesAfterBattle.get(0).getTroops().size());
    Assert.assertEquals(0,armiesAfterBattle.get(1).getTroops().size());
  }

  @Test
  public void fightOpponent_returnsArmyWithUpdatedTroops() {
    Army army1 = ArmyFactory.createAttackingArmy();
    Army army2 = ArmyFactory.createDefendingArmy();
    List<TroopEntity> damagedTroops = TroopFactory.createDefaultTroops();
    int damage = 50;

    Mockito.doReturn(damage).when(battleService).calculateIncuredDamage(army1,army2);
    Mockito.doReturn(damagedTroops).when(battleService).shareDamageAmongTroops(army1,damage);

    Army updatedArmy = battleService.fightOponent(army1,army2);

    Assert.assertEquals(damagedTroops,updatedArmy.getTroops());
  }

  @Test
  public void calculateIncuredDamage_shouldReturnCorrectDamage() {
    Army army1 = ArmyFactory.createAttackingArmy();
    army1.getTroops().get(0).setDefence(1);
    Army army2 = ArmyFactory.createDefendingArmy();

    int damage = battleService.calculateIncuredDamage(army1,army2);

    Assert.assertEquals(100, damage);
  }

  @Test
  public void calculateIncuredDamage_shouldReturnZeroDamage() {
    Army army1 = ArmyFactory.createAttackingArmy();
    Army army2 = ArmyFactory.createDefendingArmy();
    army2.getTroops().get(0).setAttack(1);

    int damage = battleService.calculateIncuredDamage(army1,army2);

    Assert.assertEquals(0, damage);
  }

  @Test
  public void shareDamageAmongTroops_returnsDamagedTroops() {
    Army army = ArmyFactory.createAttackingArmy();
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
    Army army = ArmyFactory.createAttackingArmy(); //3 troops
    army.getTroops().get(0).setHp(0);

    List<TroopEntity> aliveTroops = battleService.removeDeadTroopsFromArmy(army);

    Assert.assertEquals(2,aliveTroops.size());
  }

  @Test
  public void removeDeadTroopsFromKingdom_returnsUpdtedArmy() {
    Army army = ArmyFactory.createAttackingArmy();
    List<TroopEntity> originalListOfTroops = new ArrayList<>(); //3 troops
    originalListOfTroops.addAll(army.getTroops());
    army.getTroops().remove(0); //removing one troop from fighting troops (he died)

    Army updatedArmy = battleService.removeDeadTroopsFromKingdom(army,originalListOfTroops);

    Assert.assertEquals(2,updatedArmy.getKingdom().getTroops().size());
  }

  @Test
  public void updateArmyPointsAfterFight_returnsUpdtedArmy() {
    Army army = ArmyFactory.createAttackingArmy();
    army.setHealthPoints(10);
    army.setDefencePoints(10);
    army.setAttackPoints(10);

    Army updatedArmy = battleService.updateArmyPointsAfterFight(army);

    Assert.assertEquals(306,updatedArmy.getHealthPoints());
    Assert.assertEquals(306,updatedArmy.getDefencePoints());
    Assert.assertEquals(306,updatedArmy.getAttackPoints());
  }






}
