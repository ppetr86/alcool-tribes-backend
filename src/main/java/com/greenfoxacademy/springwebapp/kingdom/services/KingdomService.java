package com.greenfoxacademy.springwebapp.kingdom.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomNameDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface KingdomService {

    KingdomResponseDTO changeKingdomName(KingdomEntity kingdom, KingdomNameDTO nameDTO);


    KingdomResponseDTO convert(KingdomEntity e);


    KingdomResponseDTO entityToKingdomResponseDTO(Long id);


    KingdomEntity findByID(Long id);


    KingdomEntity findByPlayerId(Long id);


    String findKingdomNameByPlayerID(Long id);


    KingdomEntity saveKingdom(KingdomEntity kingdom);
}