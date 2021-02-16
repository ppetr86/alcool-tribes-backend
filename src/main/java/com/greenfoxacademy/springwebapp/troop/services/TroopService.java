package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopListResponseDto;

public interface TroopService {

  TroopListResponseDto troopsToListDTO(KingdomEntity entity);
}
