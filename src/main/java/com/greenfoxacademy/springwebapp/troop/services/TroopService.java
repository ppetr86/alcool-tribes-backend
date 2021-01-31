package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;

import java.util.Set;

public interface TroopService {

  Set<TroopEntity> findTroopsByKingdomID(Long id);
}
