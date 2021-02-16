package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenCustomException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidAcademyIdException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopRequestDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;

public interface TroopService {
  TroopEntityResponseDTO createTroop(KingdomEntity kingdom, TroopRequestDTO requestDTO) throws
      ForbiddenCustomException, InvalidAcademyIdException, NotEnoughResourceException;
}
