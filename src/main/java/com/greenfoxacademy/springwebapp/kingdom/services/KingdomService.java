package com.greenfoxacademy.springwebapp.kingdom.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;

public interface KingdomService {

  boolean hasKingdomTownhall();

  KingdomEntity findByID(Long id);

  KingdomResponseDTO kingdomResponseDTO(KingdomEntity kingdom);

  KingdomEntity findByPlayerId(Long id);

  KingdomEntity saveKingdom(KingdomEntity kingdom);

}
