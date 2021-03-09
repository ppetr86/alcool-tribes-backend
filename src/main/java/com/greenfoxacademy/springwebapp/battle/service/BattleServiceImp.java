package com.greenfoxacademy.springwebapp.battle.service;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import org.springframework.stereotype.Service;

@Service
public class BattleServiceImp implements BattleService{

  @Override
  public Integer battleHasTime(KingdomEntity ownKingdom, KingdomEntity enemyKingdom) {
    Integer ownX = ownKingdom.getLocation().getX();
    Integer ownY = ownKingdom.getLocation().getY();
    Integer enemyX = enemyKingdom.getLocation().getX();
    Integer enemyY = enemyKingdom.getLocation().getY();

    Integer differenceX = differenceBetweenTwoKingdomsXOrYPlaces(ownX, enemyX);
    Integer differenceY = differenceBetweenTwoKingdomsXOrYPlaces(ownY, enemyY);

    return differenceX + differenceY;
  }

  private Integer differenceBetweenTwoKingdomsXOrYPlaces(int ownXOrY, int enemyXOrY) {
    if ((ownXOrY <= 0 && enemyXOrY <= 0) || (0 <= ownXOrY && 0 <= enemyXOrY)){
      if (ownXOrY < enemyXOrY){
        return enemyXOrY - ownXOrY;
      } else {
        return ownXOrY - enemyXOrY;
      }
    } else if (ownXOrY <= 0){
      return enemyXOrY - ownXOrY;
    } else {
      return ownXOrY - enemyXOrY;
    }
  }
}
