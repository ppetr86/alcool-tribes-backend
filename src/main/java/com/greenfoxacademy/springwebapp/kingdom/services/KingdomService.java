package com.greenfoxacademy.springwebapp.kingdom.services;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomNameDTO;
import org.springframework.stereotype.Service;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;

@Service
public interface KingdomService {

  KingdomEntity findByID(Long id);

  KingdomResponseDTO entityToKingdomResponseDTO(Long id);

  KingdomEntity findByPlayerId(Long id);

  KingdomEntity saveKingdom(KingdomEntity kingdom);

  KingdomResponseDTO changeKingdomName(KingdomEntity kingdom, KingdomNameDTO nameDTO) throws MissingParameterException;
}