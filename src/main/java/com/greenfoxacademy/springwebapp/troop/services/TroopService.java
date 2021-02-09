package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopRequestDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopResponseDTO;

public interface TroopService {
  TroopResponseDTO createTroop(KingdomEntity kingdom, TroopRequestDTO requestDTO);
}
