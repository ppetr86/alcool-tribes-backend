package com.greenfoxacademy.springwebapp.common;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


import com.greenfoxacademy.springwebapp.common.services.TimeServiceImp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TimeServiceUnitTests {
  TimeServiceImp timeService;

  @Before
  public void setup() {
    timeService = new TimeServiceImp();
  }

  @Test
  public void getTimeReturnsCorrectTimeInSeconds() {
    //arrange
    Long minEpochTime = 1611392000L;
    Long maxEpochTime =
        1711392000L;//difference is apx 3 years, so the unit test will be valid for 3 years

    //act
    Long actualTimeInSec = timeService.getTime();

    //assert
    Assert.assertTrue(minEpochTime < actualTimeInSec);
    Assert.assertTrue(maxEpochTime > actualTimeInSec);
  }

  @Test
  public void getTimeAfterReturnsCorrectFutureTimeInSeconds() {
    //arrange
    timeService = Mockito.spy(
        TimeServiceImp.class); //spy = i want to fake only particular method from timeService, not whole class
    Mockito.doReturn(100L).when(timeService)
        .getTime(); //here i am faking just getTime() method from timeService
    //act
    long timeAfter = timeService.getTimeAfter(500);
    //assert
    Assert.assertEquals(600L, timeAfter);
  }

  @Test
  public void getTimeAfterReturnsWrongFutureTimeInSeconds() {
    //arrange
    timeService = Mockito.spy(TimeServiceImp.class);
    Mockito.doReturn(100L).when(timeService).getTime();
    //act
    long timeAfter = timeService.getTimeAfter(500);
    //assert
    Assert.assertNotEquals(500L, timeAfter);
    verify(timeService, times(1)).getTime();
  }

  @Test
  public void getTimeBetweenReturnsCorrectTimeInSeconds() {
    Long from = 1611392000L;
    Long to = 1611393000L;
    int timeBetween = timeService.getTimeBetween(from, to);
    Assert.assertEquals(1000, timeBetween);
  }

  @Test
  public void getTimeBetweenReturnsIncorrectTimeInSeconds() {
    Assert.assertNotEquals(2000, timeService.getTimeBetween(1611392000L, 1611393000L));
  }


}
