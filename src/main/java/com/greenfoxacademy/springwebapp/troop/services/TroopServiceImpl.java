package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopRequestDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class TroopServiceImpl implements TroopService {

  @Override
  public TroopResponseDTO createTroop(KingdomEntity kingdom, TroopRequestDTO requestDTO) {
    return null;
  }
}
