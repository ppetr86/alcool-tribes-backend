package com.greenfoxacademy.springwebapp.common;

import com.greenfoxacademy.springwebapp.common.services.TimeServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Instant;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TimeServiceUnitTest {

  TimeServiceImpl timeService;

  @Before
  public void setup() {
    timeService = new TimeServiceImpl();
  }

  @Test
  public void getTimeReturnsCorrectTimeInSeconds() {
    Long minEpochTime = 1611392000L;

    Long actualTimeInSec = timeService.getTime();

    Assert.assertTrue(minEpochTime < actualTimeInSec);
    Assert.assertEquals((Long) (Instant.now().getEpochSecond()), actualTimeInSec);
  }

  @Test
  public void getTimeAfterReturnsCorrectFutureTimeInSeconds() {
    timeService = Mockito.spy(TimeServiceImpl.class);
    Mockito.doReturn(100L).when(timeService)
        .getTime();

    long timeAfter = timeService.getTimeAfter(500);

    Assert.assertEquals(600L, timeAfter);
  }

  @Test
  public void getTimeAfterReturnsWrongFutureTimeInSeconds() {
    timeService = Mockito.spy(TimeServiceImpl.class);
    Mockito.doReturn(100L).when(timeService).getTime();

    long timeAfter = timeService.getTimeAfter(400);

    Assert.assertNotEquals(600L, timeAfter);
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
  public void getTimeBetweenWhenSecondNumberIsSmallerThenFirstnumberThanReturns0() {
    Assert.assertEquals(0, timeService.getTimeBetween(1611392000L, 1611391000L));
  }


}
