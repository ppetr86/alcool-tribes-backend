package com.greenfoxacademy.springwebapp.kingdom.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface KingdomService {

  KingdomEntity findByID(Long id);

  KingdomResponseDTO entityToKingdomResponseDTO(Long id);

  KingdomEntity findByPlayerId(Long id);

  KingdomEntity saveKingdom(KingdomEntity kingdom);

  String findKingdomNameByPlayerID(Long id);

}