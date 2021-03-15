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

  public int killAllTroopsInArmy(Army army) {
    List<TroopEntity> deadTroops = army.getTroops();
    final int deadCount = deadTroops.size();

    army.setTroops(new ArrayList<>()); //removing all dead troops from army troops - whole army died

    List<TroopEntity> aliveTroopsInKingdom = army.getKingdom().getTroops();
    aliveTroopsInKingdom.removeAll(deadTroops);
    army.getKingdom().setTroops(aliveTroopsInKingdom); //removing all dead troops from kingdom

    troopService.deleteListOfTroops(deadTroops); //deleting dead troops from DB

    return deadCount;
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
    List<TroopEntity> attackingTroopsBeforeFight = attackingArmy.getTroops();
    List<TroopEntity> defendingTroopsBeforeFight = defendingArmy.getTroops();

    while (attackingArmy.getTroops().size() > 0 && defendingArmy.getTroops().size() > 0) {
      for (int i = 0; i < 10; i++) {
        fightOponent(attackingArmy, defendingArmy);
        fightOponent(defendingArmy, attackingArmy);
      }
    }

    removeDeadTroopsFromKingdom(attackingArmy, attackingTroopsBeforeFight);
    removeDeadTroopsFromKingdom(defendingArmy, defendingTroopsBeforeFight);

    return  new ArrayList<>(Arrays.asList(attackingArmy,defendingArmy));
  }

  public Army fightOponent(Army army1, Army army2) {
    int armyDamage = calculateDamage(army1, army2);
    List<TroopEntity> survivedTroops = damageTroops(army1.getTroops(), armyDamage);
    army1.setTroops(survivedTroops);

    return army1;
  }


  public int calculateDamage(Army army1, Army army2) {
    return Math.max(army2.getAttackPoints() - army1.getDefencePoints(),0);
  }

  public List<TroopEntity> damageTroops(List<TroopEntity> troops, int armyDamage) {
    //getting total army defence points
    int armyDefencePoints = troops.stream().mapToInt(troop -> troop.getDefence()).sum();

    //sharing damage among troops
    List<TroopEntity> damagedTroops = shareDamageAmongTroops(troops,armyDamage,armyDefencePoints);

    //removing dead troops
    List<TroopEntity> survivedTroops = removeTroopsWithZeroHp(damagedTroops);

    return survivedTroops;
  }

  public List<TroopEntity> shareDamageAmongTroops(List<TroopEntity> troops, int armyDamage,
                                                  int armyDefencePoints) {
    List<TroopEntity> damagedTroops = troops;
    damagedTroops.stream()
        .forEach(troop -> {
          int troopDamage = (int) (armyDamage - ((armyDamage / armyDefencePoints) * troop.getDefence()));
          int troopHP = troop.getHp() - troopDamage;
          troop.setHp(troopHP > 0 ? troopHP : 0);
        });
    return damagedTroops;
  }

  public List<TroopEntity> removeTroopsWithZeroHp(List<TroopEntity> damagedTroops) {
    List<TroopEntity> survivedTroops = damagedTroops;
    survivedTroops.stream()
        .filter(troop -> troop.getHp() > 0)
        .collect(Collectors.toList());
    return survivedTroops;
  }


  public Army removeDeadTroopsFromKingdom(Army army, List<TroopEntity> troopsInArmyBeforeFight) {
    List<TroopEntity> deadTroops = troopsInArmyBeforeFight;
    deadTroops.removeAll(army.getTroops());

    List<TroopEntity> aliveKingdomTroops = army.getKingdom().getTroops();
    aliveKingdomTroops.removeAll(deadTroops);
    army.getKingdom().setTroops(aliveKingdomTroops); //removing dead troops from kingdom.

    troopService.deleteListOfTroops(deadTroops); //deleting dead troops from DB

    return army;
  }

  //"After battle" section
  public BattleResultDTO performAfterBattleActions(List<Army> armiesAfterBattle) {

    return null;
  }
}
