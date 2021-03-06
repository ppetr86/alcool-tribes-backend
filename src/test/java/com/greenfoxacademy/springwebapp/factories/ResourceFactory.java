package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.enums.ResourceType;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

public class ResourceFactory {

  public static List<ResourceEntity> createDefaultResources(KingdomEntity kingdom) {
    return Arrays.asList(
        new ResourceEntity(kingdom, ResourceType.GOLD, 100, 100, 999L),
        new ResourceEntity(kingdom, ResourceType.FOOD, 100, 100, 999L)
    );
  }

  public static List<ResourceEntity> createResourcesWithAllDataWithHighAmount() {
    long oneMinuteBefore = Instant.now().minus(1, ChronoUnit.MINUTES).getEpochSecond();
    return Arrays.asList(
        new ResourceEntity(null, ResourceType.GOLD, 1000, 100, oneMinuteBefore),
        new ResourceEntity(null, ResourceType.FOOD, 1000, 100, oneMinuteBefore)
    );
  }

  public static List<ResourceEntity> createResourcesWithAllDataWithLowAmount() {
    long oneMinuteBefore = Instant.now().minus(1, ChronoUnit.MINUTES).getEpochSecond();
    return Arrays.asList(
        new ResourceEntity(null, ResourceType.GOLD, 0, 50, oneMinuteBefore),
        new ResourceEntity(null, ResourceType.FOOD, 0, 50, oneMinuteBefore)
    );
  }

}