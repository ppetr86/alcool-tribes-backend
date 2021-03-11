package com.greenfoxacademy.springwebapp.battle.services;

import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleRequestDTO;
import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleResponseDTO;
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

  private int attackerAP = 0;
  private int attackerDP = 0;
  private int attackerHP = 0;
  private int defenderAP = 0;
  private int defenderDP = 0;
  private int defenderHP = 0;

  private final KingdomService kingdomService;

  public BattleServiceImpl(
      KingdomService kingdomService) {
    this.kingdomService = kingdomService;
  }

  //Endpoint methods
  @Override
  public BattleResponseDTO initiateBattle(Long enemyKingdomId, BattleRequestDTO requestDTO,
                                          KingdomEntity attackingKingdom)
      throws MissingParameterException, IdNotFoundException, ForbiddenActionException {

    if (enemyKingdomId == attackingKingdom.getId()) throw new ForbiddenActionException();

    KingdomEntity defendingKingdom = kingdomService.findByID(enemyKingdomId);
    if (defendingKingdom == null) throw new IdNotFoundException();

    List<TroopEntity> attackingArmy = getAttackingArmy(requestDTO, attackingKingdom);
    if (attackingArmy.isEmpty()) throw new MissingParameterException(
        "none of the provided troop IDs is available in your kingdom. Your army is empty");

    List<TroopEntity> defendingArmy = getDefendingArmy(defendingKingdom);

    prepareForBattle(attackingKingdom, defendingKingdom, attackingArmy, defendingArmy);

    return new BattleResponseDTO();
  }

  public List<TroopEntity> getAttackingArmy(BattleRequestDTO requestDTO,
                                            KingdomEntity attackingKingdom) {
    return attackingKingdom.getTroops().stream()
        .filter(troop -> Arrays.stream(requestDTO.getTroopIds())
            .filter(a -> a == troop.getId())
            .findFirst()
            .orElse(null) ==  troop.getId())
        .collect(Collectors.toList());
  }

  public List<TroopEntity> getDefendingArmy(KingdomEntity defendingKingdom) {
    return defendingKingdom.getTroops().stream()
        .filter(troop -> troop.isHome())
        .collect(Collectors.toList());
  }

  //Before Battle
  public Boolean prepareForBattle(KingdomEntity attackingKingdom, KingdomEntity defendingKingdom,
                                  List<TroopEntity> attackingArmy, List<TroopEntity> defendingArmy) {
    calculateHealthPoints(attackingKingdom, defendingKingdom, attackingArmy, defendingArmy);
    calculateAttackPoints(attackingArmy, defendingArmy);
    calculateDefencePoints(attackingArmy, defendingKingdom);

    return true;
  }

  public boolean calculateHealthPoints(KingdomEntity attackingKingdom, KingdomEntity defendingKingdom,
                                       List<TroopEntity> attackingArmy,
                                       List<TroopEntity> defendingArmy) {
    this.attackerHP = calculateAttackerHP(attackingKingdom, defendingKingdom, attackingArmy);
    if (this.attackerHP == 0) {
      log.info("Deffending Army won automatically - attacking kingdom did not survived travel!");
      //TODO: finish scenario when defending army wins automatically
      defendingArmyWins();
      return false;
    }

    if (defendingArmy.isEmpty()) {
      log.info("Attacking Army has won automatically - defending kingdom had no troops at home!");
      //TODO: finish scenario when attacking army wins automatically
      attackingArmyWins();
      return false;
    }
    this.defenderHP = defendingArmy.stream().mapToInt(troop -> troop.getHp()).sum();

    return true;
  }

  public int calculateAttackerHP(KingdomEntity attackingKingdom,
                                 KingdomEntity defendingKingdom, List<TroopEntity> attackingArmy) {
    int distance = calculateDistanceTraveled(attackingKingdom, defendingKingdom);
    int attackerArmyHP = attackingArmy.stream().mapToInt(troop -> troop.getHp()).sum();
    int finalHP = attackerArmyHP - (int)(attackerArmyHP*distance*0.02);
    return finalHP < 0 ? 0 : finalHP;
  }

  public int calculateDistanceTraveled(KingdomEntity attackingKingdom,
                                       KingdomEntity defendingKingdom) {
    int travelX = Math.abs(attackingKingdom.getLocation().getX()
        -defendingKingdom.getLocation().getX());
    int travelY = Math.abs(attackingKingdom.getLocation().getY()
        -defendingKingdom.getLocation().getY());

    return travelX + travelY;
  }

  public void calculateAttackPoints(List<TroopEntity> attackingArmy,
                                    List<TroopEntity> defendingArmy) {
    this.attackerAP = attackingArmy.stream()
        .mapToInt(troop -> troop.getAttack())
        .sum();

    this.defenderAP = defendingArmy.stream()
        .mapToInt(troop -> troop.getAttack())
        .sum();
  }

  public int calculateDefencePoints(List<TroopEntity> attackingArmy,
                                     KingdomEntity defendingKingdom) {
    float bonusDefence = calculateBonusDefence(defendingKingdom);
    return 0;
  }

  public float calculateBonusDefence(KingdomEntity defendingKingdom) {

    return 0.0f;
  }


  //TODO: finish scenario when attacking army wins automatically
  public void attackingArmyWins() {
  }

  //TODO: finish scenario when defending army wins automatically
  public void defendingArmyWins() {
  }
}
