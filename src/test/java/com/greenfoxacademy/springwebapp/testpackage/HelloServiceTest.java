package com.greenfoxacademy.springwebapp.testpackage;

import com.greenfoxacademy.springwebapp.models.Player;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HelloServiceTest {

  private PlayerService playerService;

  @Before
  public void init() {
    playerService = new PlayerService() {
      @Override
      public Player loadPlayerByUsername(String username) {
        return null;
      }
    };
  }

  @Test
  public void getHelloWorld_ValidExpectedValue_Equals() {
    Assert.assertEquals("Hello World!", playerService.loadPlayerByUsername("test"));
  }

  @Test
  public void getHelloWorld_InvalidExpectedValue_NotEquals() {
    Assert.assertNotEquals("Hello!", playerService.loadPlayerByUsername("test"));
  }
}
