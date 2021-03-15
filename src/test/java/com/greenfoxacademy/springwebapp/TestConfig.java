package com.greenfoxacademy.springwebapp;

import org.mockito.Mockito;
import org.springframework.core.env.Environment;

public class TestConfig {

  public static Environment mockEnvironment() {
    Environment env = Mockito.mock(Environment.class);
    Mockito.when(env.getProperty("building.townhall.buildingTime"))
        .thenReturn("120");
    Mockito.when(env.getProperty("building.farm.buildingTime"))
        .thenReturn("60");
    Mockito.when(env.getProperty("building.mine.buildingTime"))
        .thenReturn("60");
    Mockito.when(env.getProperty("building.academy.buildingTime"))
        .thenReturn("90");
    Mockito.when(env.getProperty("building.townhall.hp"))
        .thenReturn("200");
    Mockito.when(env.getProperty("building.farm.hp"))
        .thenReturn("100");
    Mockito.when(env.getProperty("building.mine.hp"))
        .thenReturn("100");
    Mockito.when(env.getProperty("building.academy.hp"))
        .thenReturn("150");
    return env;
  }

}
