package com.greenfoxacademy.springwebapp.battle.services;

import com.greenfoxacademy.springwebapp.battle.models.Army;
import com.greenfoxacademy.springwebapp.battle.models.BattleTimerTask;
import com.greenfoxacademy.springwebapp.battle.models.ReturnHomeTimerTask;
import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleRequestDTO;
import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleResponseDTO;
import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleResultDTO;
import com.greenfoxacademy.springwebapp.battle.models.enums.ArmyType;
import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.location.services.LocationService;
import com.greenfoxacademy.springwebapp.resource.models.enums.ResourceType;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import com.greenfoxacademy.springwebapp.troop.services.TroopService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class BattleServiceImpl implements BattleService {

  private final KingdomService kingdomService;
  private final BuildingService buildingService;
  private final TroopService troopService;
  private final Environment env;
  private final ResourceService resourceService;
  private final LocationService locationService;

  @Override
  public BattleResponseDTO war(Long enemyKingdomId, BattleRequestDTO requestDTO,
                               KingdomEntity attackingKingdom)
      throws MissingParameterException, IdNotFoundException, ForbiddenActionException {

    if (enemyKingdomId.equals(attackingKingdom.getId())) throw new ForbiddenActionException();

    KingdomEntity defendingKingdom = kingdomService.findByID(enemyKingdomId);
    if (defendingKingdom == null) throw new IdNotFoundException();

    List<TroopEntity> attackingTroops = getAttackingTroops(requestDTO, attackingKingdom);
    if (attackingTroops.isEmpty()) throw new MissingParameterException(
        "none of the provided troop IDs is available in your kingdom. Your army is empty");

    int distance = locationService.calculateDistanceBetweenTwoKingdoms(attackingKingdom, defendingKingdom);
    scheduleBattle(attackingKingdom, attackingTroops, defendingKingdom, distance);

    return new BattleResponseDTO();
  }

  public void scheduleBattle(KingdomEntity attackingKingdom, List<TroopEntity> attackingTroops,
                             KingdomEntity defendingKingdom, int distance) {

    BattleTimerTask battleTimerTask = new BattleTimerTask(
        attackingKingdom, attackingTroops, defendingKingdom, distance, this);
    Timer timer = createTimer();
    timer.schedule(battleTimerTask, distance);
    //BattleResultDTO battleResultDTO = runBattle(attackingKingdom, attackingTroops, defendingKingdom, distance);

    //TODO: this method will be replaced by different code when
    //set the troops that they are not home (later - after peter has this method ready)
  }

  public BattleResultDTO runBattle(KingdomEntity attackingKingdom, List<TroopEntity> attackingTroops,
                         KingdomEntity defendingKingdom, int distance) {
    Army attackingArmy = prepareAttackingArmy(attackingTroops, attackingKingdom, distance);
    Army defendingArmy = prepareDefendingArmy(defendingKingdom);
    List<Army> armiesAfterBattle = fightArmies(attackingArmy, defendingArmy);
    return performAfterBattleActions(armiesAfterBattle, distance);
  }

  //"Prepare attacking army" section
  public Army prepareAttackingArmy(List<TroopEntity> attackingTroops, KingdomEntity attackingKingdom,
                                   int distance) {
    Army attackingArmy = new Army();
    attackingArmy.setKingdom(attackingKingdom);
    attackingArmy.setTroops(attackingTroops);
    attackingArmy.setHealthPoints(calculateHPforAttackingArmy(attackingArmy, distance));
    attackingArmy.setAttackPoints(calculateAttackPoints(attackingTroops));
    attackingArmy.setDefencePoints(calculateDPforAttackingArmy(attackingTroops));
    attackingArmy.setArmyType(ArmyType.ATTACKINGARMY);

    return attackingArmy;
  }

  public List<TroopEntity> getAttackingTroops(BattleRequestDTO requestDTO,
                                              KingdomEntity attackingKingdom) {
    return attackingKingdom.getTroops().stream()
        .filter(troop -> (Arrays.stream(requestDTO.getTroopIds())
            .filter(a -> a.equals(troop.getId()))
            .findFirst()
            .orElse(null)) == troop.getId())
        .collect(Collectors.toList());
  }

  public int calculateHPforAttackingArmy(Army attackingArmy, int distance) {
    Army armyAfterTravel = applyHpLossDueToTravelling(attackingArmy, distance);

    int armyHP = armyAfterTravel.getTroops().stream().mapToInt(troop -> troop.getHp()).sum();

    if (armyHP <= 0) {
      log.info("Attacking army did not survive the travel to the enemy!");
      return 0;
    }

    return armyHP;
  }

  public Army applyHpLossDueToTravelling(Army attackingArmy, int distance) {
    List<TroopEntity> attackingTroopsBeforeTravel = new ArrayList<>();
    attackingTroopsBeforeTravel.addAll(attackingArmy.getTroops());

    //1. each troop looses 2% of his original HP per distance
    for (TroopEntity troop: attackingArmy.getTroops()) {
      int troopHpLoss = Math.round(troop.getHp() * distance * 0.02f);
      troop.setHp(troop.getHp() - troopHpLoss);
    }

    //2. removing dead troops from Army
    removeDeadTroopsFromArmy(attackingArmy);

    //3.removing dead troops from Kingdom and also from database
    removeDeadTroopsFromKingdom(attackingArmy,attackingTroopsBeforeTravel);

    return attackingArmy;
  }

  public int calculateAttackPoints(List<TroopEntity> troops) {
    return troops.stream().mapToInt(troop -> troop.getAttack()).sum();
  }

  public int calculateDPforAttackingArmy(List<TroopEntity> attackingTroops) {
    return attackingTroops.stream().mapToInt(troop -> troop.getDefence()).sum();
  }

  //"Prepare defending army" section
  public Army prepareDefendingArmy(KingdomEntity defendingKingdom) {
    Army defendingArmy = new Army();

    defendingArmy.setKingdom(defendingKingdom);
    defendingArmy.setTroops(getDefendingTroops(defendingKingdom));
    defendingArmy.setHealthPoints(calculateHPforDefendingArmy(defendingArmy.getTroops()));
    defendingArmy.setAttackPoints(calculateAttackPoints(defendingArmy.getTroops()));
    defendingArmy.setDefencePoints(calculateDPforDefendingArmy(defendingArmy));

    defendingArmy.setArmyType(ArmyType.DEFENDINGARMY);

    return defendingArmy;
  }

  public List<TroopEntity> getDefendingTroops(KingdomEntity defendingKingdom) {
    //TODO: adjust when isHome method is available. Only troops which are home are selected.
    return defendingKingdom.getTroops();
  }

  public int calculateHPforDefendingArmy(List<TroopEntity> defendingTroops) {
    if (defendingTroops.isEmpty()) {
      log.info("Defending kingdom has no troops at home to fight attacking army!");
      return 0;
    }

    return defendingTroops.stream().mapToInt(troop -> troop.getHp()).sum();
  }

  public int calculateDPforDefendingArmy(Army defendingArmy) {
    double defenceBonusCoef = calculateDefenceBonusCoeficient(defendingArmy.getKingdom());
    //calculating dp for whole army
    int armyDP = defendingArmy.getTroops().stream().mapToInt(troop -> troop.getDefence()).sum();
    int armyDpBonus = (int)(armyDP * defenceBonusCoef);
    int armyDPincludingBonus = armyDP + armyDpBonus;

    //setting new DP for each defending troop
    defendingArmy.getTroops().stream()
        .forEach(troop -> {
          double troopShare = (double)troop.getDefence() / (double)armyDP;
          troop.setDefence((int)(troopShare * armyDPincludingBonus));
        });

    return armyDPincludingBonus;
  }

  public double calculateDefenceBonusCoeficient(KingdomEntity kingdom) {
    BuildingEntity townhall = buildingService.findBuildingWithHighestLevel(kingdom,
        BuildingType.TOWNHALL);
    BuildingEntity academy = buildingService.findBuildingWithHighestLevel(kingdom,
        BuildingType.ACADEMY);
    return townhall.getLevel() * 0.02 + academy.getLevel() * 0.01;
  }

  //"Fight Armies" section

  public List<Army> fightArmies(Army attackingArmy, Army defendingArmy) {
    List<TroopEntity> attackingTroopsBeforeFight = new ArrayList<>();
    attackingTroopsBeforeFight.addAll(attackingArmy.getTroops());
    List<TroopEntity> defendingTroopsBeforeFight = new ArrayList<>();
    defendingTroopsBeforeFight.addAll(defendingArmy.getTroops());
    //after each round dead troops are removed from Army (but not from Kingdom - this is done in the end)
    for (int i = 0; i < 10; i++) {
      fightOponent(attackingArmy, defendingArmy);
      fightOponent(defendingArmy, attackingArmy);
      removeDeadTroopsFromArmy(attackingArmy);
      removeDeadTroopsFromArmy(defendingArmy);
      if (attackingArmy.getTroops().size() <= 0 || defendingArmy.getTroops().size() <= 0) break;
    }
    removeDeadTroopsFromKingdom(attackingArmy, attackingTroopsBeforeFight);
    removeDeadTroopsFromKingdom(defendingArmy, defendingTroopsBeforeFight);
    updateArmyPointsAfterFight(attackingArmy);
    updateArmyPointsAfterFight(defendingArmy);
    return  Arrays.asList(attackingArmy, defendingArmy);
  }

  public Army fightOponent(Army army1, Army army2) {
    int absorbedDamage = calculateIncuredDamage(army1, army2);
    shareDamageAmongTroops(army1, absorbedDamage);

    return army1;
  }

  public int calculateIncuredDamage(Army army1, Army army2) {
    int army1DefencePoints = army1.getTroops().stream().mapToInt(troop -> troop.getDefence()).sum();
    int army2AttackPoints = army2.getTroops().stream().mapToInt(troop -> troop.getAttack()).sum();

    return Math.max(army2AttackPoints - army1DefencePoints, 0);
  }

  /* note: damage is distributed to troops based on calculated "shares". Troop with max DP represents 1 share.
  Weaker troops are assigned more shares. General formula for number of shares is: maxDP/troop´s defence points.
  All these shares of individual troops are summed as totalShares, which allows calculation of damage per share.
  Each troop is finally calculated his portion of damage (troopSharesOnDamage*damagePerShare).
  This damage is then substracted from his health points. */

  public List<TroopEntity> shareDamageAmongTroops(Army army, int incuredDamage) {
    List<TroopEntity> troops = army.getTroops();
    int maxDP = troops.stream().mapToInt(troop -> troop.getDefence()).max().orElse(0);
    float totalShares = 0;
    for (TroopEntity troop : troops) {
      totalShares += (float) maxDP / troop.getDefence();
    }
    float damagePerShare = (float) incuredDamage / totalShares;

    troops.stream()
        .forEach(troop -> {
          float troopSharesOnDamage = (float) maxDP / troop.getDefence();
          int troopDamage = Math.round(damagePerShare * troopSharesOnDamage);
          int troopHP = troop.getHp() - troopDamage;
          troop.setHp(Math.max(troopHP, 0));
        });

    return troops;
  }

  public List<TroopEntity> removeDeadTroopsFromArmy(Army army) {
    List<TroopEntity> aliveTroops = army.getTroops().stream()
        .filter(troop -> troop.getHp() > 0)
        .collect(Collectors.toList());

    army.setTroops(aliveTroops);

    return army.getTroops();
  }


  public Army removeDeadTroopsFromKingdom(Army army, List<TroopEntity> originalListOfTroops) {
    //1.creating list of deadTroops
    List<TroopEntity> deadTroops = originalListOfTroops;
    deadTroops.removeAll(army.getTroops());

    //2.removing dead troops from kingdom
    List<TroopEntity> aliveKingdomTroops = army.getKingdom().getTroops();
    aliveKingdomTroops.removeAll(deadTroops);
    army.getKingdom().setTroops(aliveKingdomTroops);

    //3.deleting dead troops from DB
    troopService.deleteListOfTroops(deadTroops);

    return army;
  }

  public Army updateArmyPointsAfterFight(Army army) {
    int ap = 0;
    int dp = 0;
    int hp = 0;

    for (TroopEntity troop : army.getTroops()) {
      ap += troop.getAttack();
      dp += troop.getDefence();
      hp += troop.getHp();
    }

    army.setAttackPoints(ap);
    army.setDefencePoints(dp);
    army.setHealthPoints(hp);

    return army;
  }

  //Mark's section
  //"After battle" section
  public BattleResultDTO performAfterBattleActions(List<Army> armiesAfterBattle,
                                                   int distance) {
    Army attackingArmy = getArmyByType(armiesAfterBattle, ArmyType.ATTACKINGARMY);
    Army defendingArmy = getArmyByType(armiesAfterBattle, ArmyType.DEFENDINGARMY);
    if (nobodyWon(defendingArmy, attackingArmy)) {
      return new BattleResultDTO("Every Troops dead");
    } else if (defKingdomWon(defendingArmy, attackingArmy)) {
      healUpAliveTroops(defendingArmy.getTroops());
      return new BattleResultDTO("Defending Kingdom won");
    }
    if (defendingArmy.getHealthPoints() > 0) healUpAliveTroops(defendingArmy.getTroops());
    int stolenFood = calculateStolenResource(defendingArmy, attackingArmy, ResourceType.FOOD, ResourceType.GOLD);
    int stolenGold = calculateStolenResource(defendingArmy, attackingArmy, ResourceType.GOLD, ResourceType.FOOD);
    if (attackingArmy.getHealthPoints() > 0) {
      modifyDefendingKingdomResources(defendingArmy, stolenFood, stolenGold);
      scheduleReturnHome(attackingArmy, stolenFood, stolenGold, distance);
      return new BattleResultDTO("Attacking Kingdom won", stolenFood, stolenGold);
    }
    return null;
  }

  private void scheduleReturnHome(Army attackingArmy, int foodChange, int goldChange, int distance) {
    Army troopsWhoCanALiveBackTravel = applyHpLossDueToTravelling(attackingArmy, distance);
    if (0 < troopsWhoCanALiveBackTravel.getHealthPoints()) {
      ReturnHomeTimerTask returnHomeTimerTask = new ReturnHomeTimerTask(attackingArmy, foodChange, goldChange, this);
      Timer timer = createTimer();
      timer.schedule(returnHomeTimerTask, distance);
      healUpAliveTroops(troopsWhoCanALiveBackTravel.getTroops());
    }
  }

  public boolean nobodyWon(Army defendingArmy, Army attackingArmy) {
    return (defendingArmy.getHealthPoints() == 0 && attackingArmy.getHealthPoints() == 0);
  }

  public boolean defKingdomWon(Army defendingArmy, Army attackingArmy) {
    return (defendingArmy.getHealthPoints() > 0 && attackingArmy.getHealthPoints() == 0);
  }

  public int calculateStolenResource(Army defendingArmy, Army attackingArmy, ResourceType stolen,
                                     ResourceType notStolen) {
    Integer actualStolenResourceAmount =
        resourceService.calculateActualResource(defendingArmy.getKingdom(), stolen);
    Integer actualNotStolenResourceAmount =
        resourceService.calculateActualResource(defendingArmy.getKingdom(), notStolen);

    int halfOfRemainAttackArmyHP = attackingArmy.getHealthPoints() / 2;

    if (halfOfRemainAttackArmyHP <= actualStolenResourceAmount
        && halfOfRemainAttackArmyHP <= actualNotStolenResourceAmount) {
      return halfOfRemainAttackArmyHP;
    } else if (actualNotStolenResourceAmount <= halfOfRemainAttackArmyHP
        && halfOfRemainAttackArmyHP <= actualStolenResourceAmount) {
      if (halfOfRemainAttackArmyHP * 2 <= actualStolenResourceAmount) {
        return halfOfRemainAttackArmyHP * 2 - actualNotStolenResourceAmount;
      }
    }
    return actualStolenResourceAmount;
  }

  public void modifyDefendingKingdomResources(Army defendingArmy, int foodChange, int goldChange) {
    resourceService.updateResourceAmount(defendingArmy.getKingdom(), -(foodChange), ResourceType.FOOD);
    resourceService.updateResourceAmount(defendingArmy.getKingdom(), -(goldChange), ResourceType.GOLD);
  }

  public void modifyAttackingKingdomResources(Army attackingArmy, int foodChange, int goldChange) {
    resourceService.updateResourceAmount(attackingArmy.getKingdom(), foodChange, ResourceType.FOOD);
    resourceService.updateResourceAmount(attackingArmy.getKingdom(), goldChange, ResourceType.GOLD);

  }

  public Timer createTimer() {
    return new Timer();
  }

  public Army getArmyByType(List<Army> armiesAfterBattle, ArmyType type) {
    return armiesAfterBattle.stream()
        .filter(army -> army.getArmyType().equals(type))
        .findFirst()
        .orElse(null);
  }

  public Integer defineTroopHp() {
    return Integer.valueOf(Objects.requireNonNull(env.getProperty("troop.hp")));
  }

  private void healUpAliveTroops(List<TroopEntity> troops) {
    for (int i = 0; i < troops.size(); i++) {
      TroopEntity troop = troops.get(i);
      if (troop.getHp() < defineTroopHp()) {
        troop.setHp(troop.getLevel() * defineTroopHp());
      }
      troopService.saveAllTroops(troops);
    }
  }
}
