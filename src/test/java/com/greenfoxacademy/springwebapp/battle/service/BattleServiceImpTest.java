package com.greenfoxacademy.springwebapp.battle.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BattleServiceImpTest {

  private BattleServiceImp battleService;

  @Before
  public void init() {
    battleService = new BattleServiceImp();
    battleService = Mockito.spy(BattleServiceImp.class);
  }

  @Test
  public void differenceBetweenTwoKingdomsXOrYPlacesShouldReturnCorrectXValuesIfDefendXHigher() {
    int attackX = 3;
    int defendX = 10;

    Integer result = battleService.differenceBetweenTwoKingdomsXOrYPlaces(attackX, defendX);

    Assert.assertEquals(java.util.Optional.of(7), java.util.Optional.of(result));
  }

  @Test
  public void differenceBetweenTwoKingdomsXOrYPlacesShouldReturnCorrectXValuesIfAttackXHigher() {
    int attackX = 20;
    int defendX = 7;

    Integer result = battleService.differenceBetweenTwoKingdomsXOrYPlaces(attackX, defendX);

    Assert.assertEquals(java.util.Optional.of(13), java.util.Optional.of(result));
  }

  @Test
  public void differenceBetweenTwoKingdomsXOrYPlacesShouldReturnCorrectXValuesIfAttackXIsMinus() {
    int attackX = -10;
    int defendX = 10;

    Integer result = battleService.differenceBetweenTwoKingdomsXOrYPlaces(attackX, defendX);

    Assert.assertEquals(java.util.Optional.of(20), java.util.Optional.of(result));
  }

  @Test
  public void differenceBetweenTwoKingdomsXOrYPlacesShouldReturnCorrectXValuesIfDefendXIsMinus() {
    int attackX = 10;
    int defendX = -110;

    Integer result = battleService.differenceBetweenTwoKingdomsXOrYPlaces(attackX, defendX);

    Assert.assertEquals(java.util.Optional.of(120), java.util.Optional.of(result));
  }

  @Test
  public void differenceBetweenTwoKingdomsXOrYPlacesShouldReturnCorrectXValuesIfBothXIsMinus() {
    int attackX = -10;
    int defendX = -180;

    Integer result = battleService.differenceBetweenTwoKingdomsXOrYPlaces(attackX, defendX);

    Assert.assertEquals(java.util.Optional.of(170), java.util.Optional.of(result));
  }

}
