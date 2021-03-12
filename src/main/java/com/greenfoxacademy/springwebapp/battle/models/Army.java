package com.greenfoxacademy.springwebapp.battle.models;

import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Army {
  private int attackPoints = 0;
  private int defencePoints = 0;
  private int healthPoints = 0;
  private List<TroopEntity> troops;
}
