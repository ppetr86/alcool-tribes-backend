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
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import com.greenfoxacademy.springwebapp.troop.services.TroopService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class BattleServiceImpl implements BattleService {
  private final KingdomService kingdomService;
  private final BuildingService buildingService;
  private final TroopService troopService;

  @Override
  public BattleResponseDTO goToWar(Long enemyKingdomId, BattleRequestDTO requestDTO,
                                   KingdomEntity attackingKingdom)
      throws MissingParameterException, IdNotFoundException, ForbiddenActionException {

    if (enemyKingdomId == attackingKingdom.getId()) throw new ForbiddenActionException();

    KingdomEntity defendingKingdom = kingdomService.findByID(enemyKingdomId);
    if (defendingKingdom == null) throw new IdNotFoundException();

    List<TroopEntity> attackingTroops = getAttackingTroops(requestDTO, attackingKingdom);
    if (attackingTroops.isEmpty()) throw new MissingParameterException(
        "none of the provided troop IDs is available in your kingdom. Your army is empty");

    int delay = scheduleBattle(attackingKingdom, attackingTroops, defendingKingdom);

    return new BattleResponseDTO();
  }

  //"scheduling the battle
  public int scheduleBattle(KingdomEntity attackingKingdom, List<TroopEntity> attackingTroops,
                            KingdomEntity defendingKingdom) {

    //calculate distance

    //do the delay logic here such as in case of ResourceServiceImpl - doResourceUpdate
    //you will be delaying this method: runBattle and passing 5 variables into it using custom BattleTimerTask

    //set the troops that they are not home (later - after peter has this method ready)

    return 1;
  }

  private void runBattle(KingdomEntity attackingKingdom, List<TroopEntity> attackingTroops,
                         KingdomEntity defendingKingdom, int distance) {
    Army attackingArmy = prepareAttackingArmy(attackingTroops, attackingKingdom, distance);
    Army defendingArmy = prepareDefendingArmy(defendingKingdom);
    List<Army> armiesAfterBattle = fightArmies(attackingArmy, defendingArmy);
    BattleResultDTO resultDTO = performAfterBattleActions(armiesAfterBattle);
  }

  //"Prepare attacking army" section
  public Army prepareAttackingArmy(List<TroopEntity> attackingTroops,KingdomEntity attackingKingdom,
                                   int distance) {
    Army attackingArmy = new Army();
    attackingArmy.setTroops(attackingTroops);
    attackingArmy.setHealthPoints(calculateHPforAttackingArmy(attackingTroops,distance));
    attackingArmy.setAttackPoints(calculateAttackPoints(attackingTroops));
    attackingArmy.setDefencePoints(calculateDPforAttackingArmy(attackingTroops));
    attackingArmy.setKingdom(attackingKingdom);
    attackingArmy.setArmyType(ArmyType.ATTACKINGARMY);

    return attackingArmy;
  }

  public List<TroopEntity> getAttackingTroops(BattleRequestDTO requestDTO,
                                              KingdomEntity attackingKingdom) {
    return attackingKingdom.getTroops().stream()
        .filter(troop -> (Arrays.stream(requestDTO.getTroopIds())
            .filter(a -> a == troop.getId())
            .findFirst()
            .orElse(null)) ==  troop.getId())
        .collect(Collectors.toList());
  }

  public int calculateHPforAttackingArmy(List<TroopEntity> attackingTroops, int distance) {
    int armyHP = attackingTroops.stream().mapToInt(troop -> troop.getHp()).sum();
    int hpLoss = (int)(armyHP * distance * 0.02);
    int finalHP = armyHP - hpLoss;

    if (finalHP <= 0) {
      killTroops(attackingTroops); //deleting dead troops from DB
      attackingTroops = new ArrayList<>();
      log.info("Attacking army did not survive the travel to the enemy!");

      return 0;
    }

    return finalHP;
  }

  public List<Long> killTroops(List<TroopEntity> attackingTroops) {
    List<Long> deadTroops = attackingTroops.stream()
        .map(troop -> troop.getId())
        .collect(Collectors.toList());
    troopService.deleteMoreTroopsById(deadTroops);
    return deadTroops;
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
    List<TroopEntity> defendingTroops = getDefendingTroops(defendingKingdom);

    defendingArmy.setTroops(defendingTroops);
    defendingArmy.setHealthPoints(alculateHPforDefendingArmy(defendingTroops));
    defendingArmy.setAttackPoints(calculateAttackPoints(defendingTroops));
    defendingArmy.setDefencePoints(calculateDPforDefendingArmy(defendingTroops, defendingKingdom));
    defendingArmy.setKingdom(defendingKingdom);
    defendingArmy.setArmyType(ArmyType.DEFENDINGARMY);

    return defendingArmy;
  }

  public List<TroopEntity> getDefendingTroops(KingdomEntity defendingKingdom) {
    //TODO: adjust when isHome method is available. Only troops which are home are selected.
    return defendingKingdom.getTroops();
  }

  public int alculateHPforDefendingArmy(List<TroopEntity> defendingTroops) {
    if (defendingTroops.isEmpty()) {
      log.info("Attacking Army has won automatically - defending kingdom has no troops at home!");

      //TODO: finish scenario when attacking army wins automatically
    }
    return defendingTroops.stream().mapToInt(troop -> troop.getHp()).sum();
  }

  public int calculateDPforDefendingArmy(List<TroopEntity> defendingTroops,
                                         KingdomEntity kingdom) {
    int armyDP = defendingTroops.stream().mapToInt(troop -> troop.getDefence()).sum();
    int dpBonus = (int)(armyDP * calculateDefenceBonusCoeficient(kingdom));
    return armyDP + dpBonus;
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

    //fighting

    return  new ArrayList<>(Arrays.asList(attackingArmy,defendingArmy));
  }

  //"After battle" section
  public BattleResultDTO performAfterBattleActions(List<Army> armiesAfterBattle) {

    return null;
  }
}
