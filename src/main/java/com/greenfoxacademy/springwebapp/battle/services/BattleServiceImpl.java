package com.greenfoxacademy.springwebapp.battle.services;

import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleRequestDTO;
import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleResponseDTO;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import org.springframework.stereotype.Service;

@Service
public class BattleServiceImpl implements BattleService {
  @Override
  public BattleResponseDTO initiateBattle(Long enemyKingdomId, BattleRequestDTO requestDTO,
                                          KingdomEntity kingdom)
      throws IdNotFoundException, ForbiddenActionException {
    return null;
  }
}
