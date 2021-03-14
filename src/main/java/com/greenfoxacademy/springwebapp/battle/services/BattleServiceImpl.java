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
  public BattleResponseDTO war(Long enemyKingdomId, BattleRequestDTO requestDTO,
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
            .orElse(null)) ==  troop.getId())
        .collect(Collectors.toList());
  }

  public int calculateHPforAttackingArmy(Army attackingArmy, int distance) {
    int armyHP = attackingArmy.getTroops().stream().mapToInt(troop -> troop.getHp()).sum();
    int hpLoss = (int)(armyHP * distance * 0.02);
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
    int armyDP = defendingArmy.getTroops().stream().mapToInt(troop -> troop.getDefence()).sum();
    int dpBonus = (int)(armyDP * calculateDefenceBonusCoeficient(defendingArmy.getKingdom()));
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
