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

    if (enemyKingdomId == attackingKingdom.getId()) {
      throw new ForbiddenActionException();
    }

    KingdomEntity defendingKingdom = kingdomService.findByID(enemyKingdomId);
    if (defendingKingdom == null) {
      throw new IdNotFoundException();
    }

    List<TroopEntity> attackingTroops = getAttackingTroops(requestDTO, attackingKingdom);
    if (attackingTroops.isEmpty()) {
      throw new MissingParameterException(
          "none of the provided troop IDs is available in your kingdom. Your army is empty");
    }

    int delay = travelTime(attackingKingdom, attackingTroops, defendingKingdom);

    return new BattleResponseDTO();
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
    int armyHP = attackingArmy.getTroops().stream().mapToInt(troop -> troop.getHp()).sum();
    int hpLoss = (int) (armyHP * distance * 0.02);
    int finalHP = armyHP - hpLoss;

    if (finalHP <= 0) {
      killAllTroopsInArmy(attackingArmy);

      log.info("Attacking army did not survive the travel to the enemy!");

      return 0;
    }

    return finalHP;
  }


  public List<Long> killAllTroopsInArmy(Army army) {
    //collecting dead troops ids and deleting them from DB and removing them from related kingdom/army
    List<Long> deadTroopsIds = army.getTroops().stream()
        .map(troop -> troop.getId())
        .collect(Collectors.toList());

    troopService.deleteMoreTroopsById(deadTroopsIds);

    List<TroopEntity> aliveTroopsInKingdom = army.getKingdom().getTroops().stream()
        .filter(troop -> !army.getTroops().contains(troop))
        .collect(Collectors.toList());
    army.getKingdom().setTroops(aliveTroopsInKingdom); //removing all dead troops from kingdom

    army.setTroops(new ArrayList<>()); //removing all dead troops from army troops - whole army died

    return deadTroopsIds;
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
    defendingArmy.setDefencePoints(calculateDPForDefendingArmy(defendingArmy));

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

  public int calculateDPForDefendingArmy(Army defendingArmy) {
    int armyDP = defendingArmy.getTroops().stream().mapToInt(troop -> troop.getDefence()).sum();
    int dpBonus = (int) (armyDP * calculateDefenceBonusCoeficient(defendingArmy.getKingdom()));
    return armyDP + dpBonus;
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

    //fighting

    return new ArrayList<>(Arrays.asList(attackingArmy, defendingArmy));
  }

  //"Do battle" section
  public List<Army> doBattle(Army attackingArmy, Army defendingArmy) {

    //do battle

    return new ArrayList<>(Arrays.asList(attackingArmy, defendingArmy));
  }

  public void healAliveTroops(Army army) {
    army.getTroops()
        .forEach(troop -> troop.setHp(troop.getLevel() * defineTroopHp()));
    troopService.saveAllTroops(army.getTroops());
  }

  public void killTroopWhichCanNotReachHome(Army army, int distance) {
    List<TroopEntity> deadTroops = army.getTroops().stream()
        .filter(troop -> troop.getHp() < troop.getHp() * distance * 0.02)
        .collect(Collectors.toList());
    troopService.deleteAllTroops(deadTroops);
  }

  public ResourceEntity calculateStolenResources(Army defendingArmy, Army attackingArmy) {
    ResourceEntity resourceEntity = resourceService.getResourceByResourceType(defendingArmy.getKingdom(), ResourceType.FOOD);
    int resourceAmount = resourceEntity.getAmount();

    int remainArmyHP = attackingArmy.getTroops().stream()
        .mapToInt(troop -> troop.getHp()).sum();

    int halfOfRemainAttackArmyHP = remainArmyHP / 2;

    if (halfOfRemainAttackArmyHP < resourceAmount) {
      resourceEntity.setAmount(resourceAmount - halfOfRemainAttackArmyHP);
    } else {
      resourceEntity.setAmount(resourceAmount);
    }
    return resourceEntity;
  }

  public void calculateStolenGold(Army defendingArmy, Army attackingArmy) {
    ResourceEntity defendingKingdomFood =
        resourceService.getResourceByResourceType(defendingArmy.getKingdom(), ResourceType.FOOD);
    ResourceEntity defendingKingdomGold =
        resourceService.getResourceByResourceType(defendingArmy.getKingdom(), ResourceType.GOLD);

    ResourceEntity attackingKingdomFood =
        resourceService.getResourceByResourceType(attackingArmy.getKingdom(), ResourceType.FOOD);
    ResourceEntity attackingKingdomGold =
        resourceService.getResourceByResourceType(attackingArmy.getKingdom(), ResourceType.GOLD);

    int defKingGolds = defendingKingdomGold.getAmount();
    int defKingFoods = defendingKingdomFood.getAmount();

    int remainArmyHP = attackingArmy.getTroops().stream()
        .mapToInt(troop -> troop.getHp()).sum();

    int halfOfRemainAttackArmyHP = remainArmyHP / 2;

    List<ResourceEntity> resourcesList = new ArrayList<>();

    if (halfOfRemainAttackArmyHP < defKingFoods && halfOfRemainAttackArmyHP < defKingGolds) {
      defendingKingdomFood.setAmount(defKingFoods - halfOfRemainAttackArmyHP);
      defendingKingdomGold.setAmount(defKingGolds - halfOfRemainAttackArmyHP);

      //TODO: here I have to use the delay(travelTime) method
      attackingKingdomFood.setAmount(attackingKingdomFood.getAmount() + halfOfRemainAttackArmyHP);
      attackingKingdomGold.setAmount(attackingKingdomGold.getAmount() + halfOfRemainAttackArmyHP);

      resourcesList.add(defendingKingdomFood);
      resourcesList.add(defendingKingdomGold);

      resourcesList.add(attackingKingdomFood);
      resourcesList.add(attackingKingdomGold);

      resourceService.saveResources(resourcesList);
    }
  }

  //"After battle" section
  public BattleResultDTO performAfterBattleActions(List<Army> armiesAfterBattle, int distance) {
    Army attackingArmy = getArmyByType(armiesAfterBattle, ArmyType.ATTACKINGARMY);
    Army defendingArmy = getArmyByType(armiesAfterBattle, ArmyType.DEFENDINGARMY);

    if (defendingArmy.getTroops() != null) {
      healAliveTroops(defendingArmy);
      if (attackingArmy.getTroops() == null) {
        return new BattleResultDTO("Defending Kingdom won");
      }
    }
    if (attackingArmy.getTroops() != null) {
      killTroopWhichCanNotReachHome(attackingArmy, distance);
      healAliveTroops(attackingArmy);
    }


    //TODO: have to calculate the the options when one resources is higher than the other
    if (defendingArmy.getTroops() == null) {
      return new BattleResultDTO("Attacking Kingdom won", halfOfRemainAttackArmyHP, halfOfRemainAttackArmyHP);
    }
    return null;
  }

  private Army getArmyByType(List<Army> armiesAfterBattle, ArmyType type) {
    return armiesAfterBattle.stream()
        .filter(army -> army.getArmyType().equals(type))
        .findFirst()
        .orElse(null);
  }

  private Integer defineTroopHp() {
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
