package com.greenfoxacademy.springwebapp.battle.services;

import com.greenfoxacademy.springwebapp.battle.models.Army;
import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleRequestDTO;
import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleResponseDTO;
import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BattleServiceImpl implements BattleService {
  private Army attackingArmy;
  private Army defendingArmy;

  private final KingdomService kingdomService;
  private final BuildingService buildingService;

  public BattleServiceImpl(KingdomService kingdomService, BuildingService buildingService) {
    this.kingdomService = kingdomService;
    this.buildingService = buildingService;
  }

  //Endpoint methods
  @Override
  public BattleResponseDTO initiateBattle(Long enemyKingdomId, BattleRequestDTO requestDTO,
                                          KingdomEntity attackingKingdom, int distance)
      throws MissingParameterException, IdNotFoundException, ForbiddenActionException {

    if (enemyKingdomId == attackingKingdom.getId()) throw new ForbiddenActionException();

    KingdomEntity defendingKingdom = kingdomService.findByID(enemyKingdomId);
    if (defendingKingdom == null) throw new IdNotFoundException();

    this.attackingArmy = prepareAttackingArmy(requestDTO, attackingKingdom, distance);
    this.defendingArmy = prepareDefendingArmy(defendingKingdom);

    return new BattleResponseDTO();
  }

  //"Prepare attacking army" section
  public Army prepareAttackingArmy(BattleRequestDTO requestDTO, KingdomEntity attackingKingdom,
                                    int distance) throws MissingParameterException {
    Army attackingArmy = new Army();
    List<TroopEntity> attackingTroops = getAttackingTroops(requestDTO, attackingKingdom);
    if (attackingTroops.isEmpty()) throw new MissingParameterException(
        "none of the provided troop IDs is available in your kingdom. Your army is empty");

    attackingArmy.setTroops(attackingTroops);
    attackingArmy.setHealthPoints(calculateHPforAttackingArmy(attackingTroops,distance));
    attackingArmy.setAttackPoints(calculateAttackPoints(attackingTroops));
    attackingArmy.setDefencePoints(calculateDPforAttackingArmy(attackingTroops));

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
      log.info("Deffending Army won automatically - attacking kingdom did not survived travel!");
      //TODO: finish scenario when defending army wins automatically
      defendingArmyWins();
      return 0;
    }

    return finalHP;
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
      attackingArmyWins();
      return 0;
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







  //TODO: finish scenario when attacking army wins automatically
  public void attackingArmyWins() {
  }

  //TODO: finish scenario when defending army wins automatically
  public void defendingArmyWins() {
  }
}
