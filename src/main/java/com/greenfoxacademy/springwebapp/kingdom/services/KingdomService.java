package com.greenfoxacademy.springwebapp.kingdom.services;


import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;

public interface KingdomService {

  boolean hasKingdomTownhall();

  KingdomEntity saveKingdom(KingdomEntity kingdom);

}
