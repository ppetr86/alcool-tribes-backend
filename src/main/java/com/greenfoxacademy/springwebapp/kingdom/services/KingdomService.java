package com.greenfoxacademy.springwebapp.kingdom.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import org.springframework.stereotype.Service;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;

import java.util.List;

@Service
public interface KingdomService {

  KingdomEntity findByID(Long id);

  KingdomResponseDTO entityToKingdomResponseDTO(Long id);

  KingdomEntity findByPlayerId(Long id);

  KingdomEntity saveKingdom(KingdomEntity kingdom);

  List<KingdomEntity> findKingdomEntitiesByLocationBetween(LocationEntity location, LocationEntity location2);

}