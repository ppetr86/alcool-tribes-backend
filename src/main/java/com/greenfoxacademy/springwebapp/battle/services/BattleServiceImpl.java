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

import org.springframework.stereotype.Service;

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

    prepareForBattle(attackingKingdom, defendingKingdom, attackingArmy);

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

  //Before Battle
  public Boolean prepareForBattle(KingdomEntity attackingKingdom, KingdomEntity defendingKingdom,
                                  List<TroopEntity> attackingArmy) {
    calculateAttackPoints(attackingArmy, defendingKingdom);
    calculateHealthPoints(attackingKingdom, attackingArmy, defendingKingdom);
    calculateDefencePoints(attackingArmy, defendingKingdom);

    return true;
  }

  public int calculateAttackPoints(List<TroopEntity> attackingArmy,
                                    KingdomEntity defendingKingdom) {

    return 0;
  }

  public int calculateHealthPoints(KingdomEntity attackingKingdom, List<TroopEntity> attackingArmy,
                                    KingdomEntity defendingKingdom) {
    int distance = calculateDistanceTraveled(attackingKingdom, defendingKingdom);

    return 0;
  }

  public int calculateDistanceTraveled(KingdomEntity attackingKingdom,
                                        KingdomEntity defendingKingdom) {

    return 0;
  }

  public int calculateDefencePoints(List<TroopEntity> attackingArmy,
                                     KingdomEntity defendingKingdom) {
    float bonusDefence = calculateBonusDefence(defendingKingdom);
    return 0;
  }

  public float calculateBonusDefence(KingdomEntity defendingKingdom) {

    return 0.0f;
  }
}
