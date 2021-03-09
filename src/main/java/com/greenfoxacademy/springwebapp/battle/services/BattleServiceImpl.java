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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BattleServiceImpl implements BattleService {
  private final KingdomService kingdomService;

  @Override
  public BattleResponseDTO initiateBattle(Long enemyKingdomId, BattleRequestDTO requestDTO,
                                          KingdomEntity attackingKingdom)
      throws MissingParameterException, IdNotFoundException, ForbiddenActionException {

    if (enemyKingdomId == null) throw new MissingParameterException("enemy kingdom ID not set");
    if (requestDTO == null) throw new MissingParameterException("troops in your army not set");
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

  public Boolean prepareForBattle(KingdomEntity attackingKingdom, KingdomEntity defendingKingdom,
                               List<TroopEntity> attackingArmy) {
    return true;
  }

}
