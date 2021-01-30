package com.greenfoxacademy.springwebapp.troop.services;


import com.greenfoxacademy.springwebapp.troop.models.KingdomEntity;

public interface KingdomService {

  boolean hasKingdomTownhall();

  KingdomEntity findByID(Long id);
}
