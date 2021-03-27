package com.greenfoxacademy.springwebapp.battle.services;

import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.factories.LocationFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BattleServiceImpTest {

  private BattleServiceImp battleService;

  @Before
  public void init() {
    battleService = new BattleServiceImp();
  }

  @Test
  public void calculateDistanceBetweenTwoKingdoms_ShouldReturn_CorrectValue_IfAllNumberArePositive() {
    KingdomEntity attackingKingdom = KingdomFactory.createKingdomEntityWithId(1L);
    attackingKingdom.setLocation(LocationFactory.createNewLocation(10, 10, attackingKingdom));
    KingdomEntity defendingKingdom = KingdomFactory.createKingdomEntityWithId(2L);
    defendingKingdom.setLocation(LocationFactory.createNewLocation(20, 20, defendingKingdom));

    Integer result = battleService.calculateDistanceBetweenTwoKingdoms(attackingKingdom, defendingKingdom);

    Assert.assertEquals(20, result.intValue());
  }

  @Test
  public void calculateDistanceBetweenTwoKingdoms_ShouldReturn_CorrectValue_IfOneNumberIsNegative() {
    KingdomEntity attackingKingdom = KingdomFactory.createKingdomEntityWithId(1L);
    attackingKingdom.setLocation(LocationFactory.createNewLocation(10, 10, attackingKingdom));
    KingdomEntity defendingKingdom = KingdomFactory.createKingdomEntityWithId(2L);
    defendingKingdom.setLocation(LocationFactory.createNewLocation(20, -20, defendingKingdom));

    Integer result = battleService.calculateDistanceBetweenTwoKingdoms(attackingKingdom, defendingKingdom);

    Assert.assertEquals(40, result.intValue());
  }

  @Test
  public void calculateDistanceBetweenTwoKingdoms_ShouldReturn_CorrectValue_IfAllNumberIsNegative() {
    KingdomEntity attackingKingdom = KingdomFactory.createKingdomEntityWithId(1L);
    attackingKingdom.setLocation(LocationFactory.createNewLocation(-3, -56, attackingKingdom));
    KingdomEntity defendingKingdom = KingdomFactory.createKingdomEntityWithId(2L);
    defendingKingdom.setLocation(LocationFactory.createNewLocation(-11, -20, defendingKingdom));

    Integer result = battleService.calculateDistanceBetweenTwoKingdoms(attackingKingdom, defendingKingdom);

    Assert.assertEquals(44, result.intValue());
  }


  @Test
  public void differenceBetweenTwoKingdomsXOrYPlacesShouldReturnCorrectXValuesIfDefendXHigher() {
    int attackX = 3;
    int defendX = 10;

    Integer result = battleService.differenceBetweenTwoKingdomsLocations(attackX, defendX);

    Assert.assertEquals(java.util.Optional.of(7), java.util.Optional.of(result));
  }

  @Test
  public void differenceBetweenTwoKingdomsXOrYPlacesShouldReturnCorrectXValuesIfAttackXHigher() {
    int attackX = 20;
    int defendX = 7;

    Integer result = battleService.differenceBetweenTwoKingdomsLocations(attackX, defendX);

    Assert.assertEquals(java.util.Optional.of(13), java.util.Optional.of(result));
  }

  @Test
  public void differenceBetweenTwoKingdomsXOrYPlacesShouldReturnCorrectXValuesIfAttackXIsMinus() {
    int attackX = -10;
    int defendX = 10;

    Integer result = battleService.differenceBetweenTwoKingdomsLocations(attackX, defendX);

    Assert.assertEquals(java.util.Optional.of(20), java.util.Optional.of(result));
  }

  @Test
  public void differenceBetweenTwoKingdomsXOrYPlacesShouldReturnCorrectXValuesIfDefendXIsMinus() {
    int attackX = 10;
    int defendX = -110;

    Integer result = battleService.differenceBetweenTwoKingdomsLocations(attackX, defendX);

    Assert.assertEquals(java.util.Optional.of(120), java.util.Optional.of(result));
  }

  @Test
  public void differenceBetweenTwoKingdomsXOrYPlacesShouldReturnCorrectXValuesIfBothXIsMinus() {
    int attackX = -10;
    int defendX = -180;

    Integer result = battleService.differenceBetweenTwoKingdomsLocations(attackX, defendX);

    Assert.assertEquals(java.util.Optional.of(170), java.util.Optional.of(result));
  }

}
