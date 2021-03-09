package com.greenfoxacademy.springwebapp.battle.service;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;

public interface BattleService {

  Integer battleHasTime(KingdomEntity ownKingdom, KingdomEntity enemyKingdom);
}
