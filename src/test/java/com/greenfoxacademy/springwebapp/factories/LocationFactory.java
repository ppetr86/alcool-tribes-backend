package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;

import java.util.ArrayList;
import java.util.List;

public class LocationFactory {

  public static List<LocationEntity> createOccupiedLocations() {
    List<LocationEntity> locationEntities = new ArrayList<>();
    locationEntities.add(new LocationEntity(1L, -65, -77, new KingdomEntity(), LocationType.KINGDOM));
    locationEntities.add(new LocationEntity(2L, -97, -63, new KingdomEntity(), LocationType.KINGDOM));
    locationEntities.add(new LocationEntity(3L, -26, -16, new KingdomEntity(), LocationType.KINGDOM));
    locationEntities.add(new LocationEntity(4L, -72, -32, new KingdomEntity(), LocationType.KINGDOM));
    locationEntities.add(new LocationEntity(5L, -38, -44, new KingdomEntity(), LocationType.KINGDOM));
    locationEntities.add(new LocationEntity(6L, -64, -38, new KingdomEntity(), LocationType.KINGDOM));
    locationEntities.add(new LocationEntity(8L, 94, -71, null, LocationType.DESERT));
    locationEntities.add(new LocationEntity(9L, 65, 36, null, LocationType.JUNGLE));
    locationEntities.add(new LocationEntity(10L, 85, 18, null, LocationType.DESERT));
    locationEntities.add(new LocationEntity(11L, -64, -74, null, LocationType.JUNGLE));
    locationEntities.add(new LocationEntity(12L, -77, -64, null, LocationType.DESERT));
    locationEntities.add(new LocationEntity(13L, 9, -60, null, LocationType.JUNGLE));
    locationEntities.add(new LocationEntity(14L, -29, -65, null, LocationType.DESERT));
    locationEntities.add(new LocationEntity(15L, 64, 14, null, LocationType.JUNGLE));
    locationEntities.add(new LocationEntity(16L, -22, -50, null, LocationType.DESERT));
    locationEntities.add(new LocationEntity(17L, -86, 20, null, LocationType.JUNGLE));
    return locationEntities;
  }
}
