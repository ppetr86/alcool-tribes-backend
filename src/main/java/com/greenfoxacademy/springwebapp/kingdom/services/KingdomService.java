package com.greenfoxacademy.springwebapp.kingdom.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import org.springframework.stereotype.Service;

@Service
public interface KingdomService {

  KingdomEntity findByPlayerId(Long id);

  KingdomEntity saveKingdom(KingdomEntity kingdom);

}
