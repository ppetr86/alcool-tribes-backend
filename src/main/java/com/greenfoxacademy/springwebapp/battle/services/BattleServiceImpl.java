package com.greenfoxacademy.springwebapp.battle.services;

import com.greenfoxacademy.springwebapp.battle.models.Army;
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
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
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

  @Override
  public BattleResponseDTO war(Long enemyKingdomId, BattleRequestDTO requestDTO,
                               KingdomEntity attackingKingdom)
      throws MissingParameterException, IdNotFoundException, ForbiddenActionException {

    if (enemyKingdomId == attackingKingdom.getId()) throw new ForbiddenActionException();

    KingdomEntity defendingKingdom = kingdomService.findByID(enemyKingdomId);
    if (defendingKingdom == null) throw new IdNotFoundException();

    List<TroopEntity> attackingTroops = getAttackingTroops(requestDTO, attackingKingdom);
    if (attackingTroops.isEmpty()) throw new MissingParameterException(
        "none of the provided troop IDs is available in your kingdom. Your army is empty");

    int delay = travelTime(attackingKingdom, attackingTroops, defendingKingdom);

    return new BattleResponseDTO();
  }

  //"scheduling the battle
  public int scheduleBattle(KingdomEntity attackingKingdom, List<TroopEntity> attackingTroops,
                            KingdomEntity defendingKingdom) {

    //calculate distance
    //TODO: change this temporary distance when you are implementing scheduleBattle
    int distance = 10;

    //do the delay logic here such as in case of ResourceServiceImpl - doResourceUpdate
    //you will be delaying this method: runBattle and passing 5 variables into it using custom BattleTimerTask
    //TODO: this method will be replaced by different code when
    runBattle(attackingKingdom, attackingTroops, defendingKingdom, distance);

    //set the troops that they are not home (later - after peter has this method ready)

    return 1;
  }

  private void runBattle(KingdomEntity attackingKingdom, List<TroopEntity> attackingTroops,
                         KingdomEntity defendingKingdom, int distance) {
    Army attackingArmy = prepareAttackingArmy(attackingTroops, attackingKingdom, distance);
    Army defendingArmy = prepareDefendingArmy(defendingKingdom);
    List<Army> armiesAfterBattle = fightArmies(attackingArmy, defendingArmy);
    BattleResultDTO resultDTO = performAfterBattleActions(armiesAfterBattle, distance);
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
            .filter(a -> a == troop.getId())
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

  //TODO: finish scenario when attacking army wins automatically
  public void attackingArmyWins() {
  }

  //TODO: finish scenario when defending army wins automatically
  public void defendingArmyWins() {
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
      i++;
    }
    removeDeadTroopsFromKingdom(attackingArmy, attackingTroopsBeforeFight);
    removeDeadTroopsFromKingdom(defendingArmy, defendingTroopsBeforeFight);
    updateArmyPointsAfterFight(attackingArmy);
    updateArmyPointsAfterFight(defendingArmy);
    return  new ArrayList<>(Arrays.asList(attackingArmy,defendingArmy));
  }

  public Army fightOponent(Army army1, Army army2) {
    int absorbedDamage = calculateIncuredDamage(army1, army2);
    shareDamageAmongTroops(army1, absorbedDamage);

    return army1;
  }

  public int calculateIncuredDamage(Army army1, Army army2) {
    int army1DefencePoints = army1.getTroops().stream().mapToInt(troop -> troop.getDefence()).sum();
    int army2AttackPoints = army2.getTroops().stream().mapToInt(troop -> troop.getAttack()).sum();

    return Math.max(army2AttackPoints - army1DefencePoints,0);
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
          troop.setHp(troopHP > 0 ? troopHP : 0);
        });

    return troops;
  }

  public List<TroopEntity> removeDeadTroopsFromArmy(Army army) {
    List<TroopEntity> deadTroops = army.getTroops().stream()
        .filter(troop -> troop.getHp() <= 0)
        .collect(Collectors.toList());

    army.getTroops().removeAll(deadTroops);

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
  public BattleResultDTO performAfterBattleActions(List<Army> armiesAfterBattle, int distance) {
    Army attackingArmy = getArmyByType(armiesAfterBattle, ArmyType.ATTACKINGARMY);
    Army defendingArmy = getArmyByType(armiesAfterBattle, ArmyType.DEFENDINGARMY);

    if (nobodyOrDefKingdomWon(defendingArmy, attackingArmy) != null) {
      return nobodyOrDefKingdomWon(defendingArmy, attackingArmy);
    }
    int stolenFood = calculateStolenResource(defendingArmy, attackingArmy, ResourceType.FOOD, ResourceType.GOLD);
    int stolenGold = calculateStolenResource(defendingArmy, attackingArmy, ResourceType.GOLD, ResourceType.FOOD);
    if (attackingArmy.getHealthPoints() > 0) {
      modifyDefendingKingdomResources(defendingArmy, stolenFood, stolenGold);
      modifyAttackingKingdomResources(attackingArmy, stolenFood, stolenGold, distance);
      if (defendingArmy.getHealthPoints() == 0) {
        return new BattleResultDTO("Attacking Kingdom won", stolenFood, stolenGold);
      }
    }
    return new BattleResultDTO("Nobody won",stolenFood, stolenGold);
  }

  public BattleResultDTO nobodyOrDefKingdomWon(Army defendingArmy, Army attackingArmy) {
    if (defendingArmy.getHealthPoints() == 0 && attackingArmy.getHealthPoints() == 0) {
      return new BattleResultDTO("Every Troops dead");
    }
    if (defendingArmy.getHealthPoints() > 0) {
      if (attackingArmy.getHealthPoints() == 0) {
        return new BattleResultDTO("Defending Kingdom won");
      }
    }
    return null;
  }

  public void killTroopWhichCanNotReachHome(Army army, int distance) {
    List<TroopEntity> deadTroops = army.getTroops().stream()
        .filter(troop -> troop.getHp() * distance * 0.02 < 1)
        .collect(Collectors.toList());

    army.getTroops().removeAll(deadTroops);
    troopService.deleteListOfTroops(deadTroops);
  }

  public int calculateStolenResource(Army defendingArmy, Army attackingArmy, ResourceType stolen,
                                     ResourceType notStolen) {
    ResourceEntity stolenResource =
        resourceService.getResourceByResourceType(defendingArmy.getKingdom(), stolen);
    ResourceEntity notStolenResource =
        resourceService.getResourceByResourceType(defendingArmy.getKingdom(), notStolen);

    int halfOfRemainAttackArmyHP = attackingArmy.getHealthPoints() / 2;

    int actualStolenResourceAmount = stolenResource.getAmount();
    int actualNotStolenResourceAmount = notStolenResource.getAmount();

    if (halfOfRemainAttackArmyHP <= actualStolenResourceAmount
        && halfOfRemainAttackArmyHP <= actualNotStolenResourceAmount) {
      return halfOfRemainAttackArmyHP;
    } else if (halfOfRemainAttackArmyHP <= actualStolenResourceAmount) {
      return halfOfRemainAttackArmyHP + (halfOfRemainAttackArmyHP - actualNotStolenResourceAmount);
    } else {
      return actualStolenResourceAmount;
    }
  }

  private void modifyDefendingKingdomResources(Army defendingArmy, int foodChange, int goldChange) {
    ResourceEntity defendingKingdomFood =
        resourceService.getResourceByResourceType(defendingArmy.getKingdom(), ResourceType.FOOD);
    ResourceEntity defendingKingdomGold =
        resourceService.getResourceByResourceType(defendingArmy.getKingdom(), ResourceType.GOLD);

    defendingKingdomFood.setAmount(defendingKingdomFood.getAmount() - foodChange);
    defendingKingdomGold.setAmount(defendingKingdomGold.getAmount() - goldChange);

    List<ResourceEntity> resources =  Arrays.asList(defendingKingdomFood, defendingKingdomGold);
    resourceService.saveResources(resources);
  }

  private void modifyAttackingKingdomResources(Army attackingArmy, int foodChange, int goldChange, int distance) {
    killTroopWhichCanNotReachHome(attackingArmy, distance);

    ResourceEntity attackingKingdomFood =
        resourceService.getResourceByResourceType(attackingArmy.getKingdom(), ResourceType.FOOD);
    ResourceEntity attackingKingdomGold =
        resourceService.getResourceByResourceType(attackingArmy.getKingdom(), ResourceType.GOLD);

    //TODO: here I have to use the delay(travelTime) method
    attackingKingdomFood.setAmount(attackingKingdomFood.getAmount() + foodChange);
    attackingKingdomGold.setAmount(attackingKingdomGold.getAmount() + goldChange);

    List<ResourceEntity> resources = Arrays.asList(attackingKingdomFood, attackingKingdomGold);
    resourceService.saveResources(resources);
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

  //scheduling the battle
  public int travelTime(KingdomEntity attackingKingdom, List<TroopEntity> attackingTroops,
                        KingdomEntity defendingKingdom) {

    //calculate distance

    //do the delay logic here such as in case of ResourceServiceImpl - doResourceUpdate
    //you will be delaying this method: runBattle and passing 5 variables into it using custom BattleTimerTask

    //set the troops that they are not home (later - after peter has this method ready)

    return 1;
  }
}
