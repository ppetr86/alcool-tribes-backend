package com.greenfoxacademy.springwebapp;

import org.mockito.Mockito;
import org.springframework.core.env.Environment;

public class TestConfig {

  public static Environment mockEnvironment() {
    Environment env = Mockito.mock(Environment.class);

    mockEnvironmentBuildingTime(env);
    mockEnvironmentHp(env);
    mockEnvironmentBuildingCosts(env);
    return env;
  }

  public static void mockEnvironmentBuildingTime(Environment env) {
    Mockito.when(env.getProperty("building.townhall.buildingTime"))
        .thenReturn("120");
    Mockito.when(env.getProperty("building.farm.buildingTime"))
        .thenReturn("60");
    Mockito.when(env.getProperty("building.mine.buildingTime"))
        .thenReturn("60");
    Mockito.when(env.getProperty("building.academy.buildingTime"))
        .thenReturn("90");
  }

  public static void mockEnvironmentHp(Environment env) {
    Mockito.when(env.getProperty("building.townhall.hp"))
        .thenReturn("200");
    Mockito.when(env.getProperty("building.farm.hp"))
        .thenReturn("100");
    Mockito.when(env.getProperty("building.mine.hp"))
        .thenReturn("100");
    Mockito.when(env.getProperty("building.academy.hp"))
        .thenReturn("150");
  }

  public static void mockEnvironmentBuildingCosts(Environment env) {
    Mockito.when(env.getProperty("building.townhall.buildingCosts"))
        .thenReturn("200");
    Mockito.when(env.getProperty("building.farm.buildingCosts"))
        .thenReturn("100");
    Mockito.when(env.getProperty("building.mine.buildingCosts"))
        .thenReturn("100");
    Mockito.when(env.getProperty("building.academy.buildingCosts"))
        .thenReturn("100");
  }
}
