package com.greenfoxacademy.springwebapp.battle.service;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import org.springframework.stereotype.Service;

@Service
public class BattleServiceImp implements BattleService{

  @Override
  public Integer battleHasTime(KingdomEntity attackingKingdom, KingdomEntity defendingKingdom) {
    Integer attackX = attackingKingdom.getLocation().getX();
    Integer attackY = attackingKingdom.getLocation().getY();
    Integer defendX = defendingKingdom.getLocation().getX();
    Integer defendY = defendingKingdom.getLocation().getY();

    Integer differenceX = differenceBetweenTwoKingdomsXOrYPlaces(attackX, defendX);
    Integer differenceY = differenceBetweenTwoKingdomsXOrYPlaces(attackY, defendY);

    return differenceX + differenceY;
  }

  public Integer differenceBetweenTwoKingdomsXOrYPlaces(int attackXOrY, int defendXOrY) {
    if ((attackXOrY <= 0 && defendXOrY <= 0) || (0 <= attackXOrY && 0 <= defendXOrY)){
      if (attackXOrY < defendXOrY){
        return defendXOrY - attackXOrY;
      } else {
        return attackXOrY - defendXOrY;
      }
    } else if (attackXOrY <= 0){
      return defendXOrY - attackXOrY;
    } else {
      return attackXOrY - defendXOrY;
    }
  }
}
