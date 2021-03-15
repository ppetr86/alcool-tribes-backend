package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocationFactory {

  public static List<LocationEntity> createOccupiedLocations() {
    List<LocationEntity> locationEntities = new ArrayList<>();
    int rows = 21;
    int cols = 21;
    int id = 0;

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        locationEntities.add(new LocationEntity((long) ++id, j - cols / 2, i - rows / 2, null, LocationType.EMPTY));
        id++;
      }
    }
    return locationEntities;
  }

  public static List<LocationEntity> createKingdoms() {
    List<LocationEntity> kingdoms = Arrays.asList(
        new LocationEntity(0, 2, new KingdomEntity(), LocationType.KINGDOM),
        new LocationEntity(0, 4, new KingdomEntity(), LocationType.KINGDOM),
        new LocationEntity(0, 6, new KingdomEntity(), LocationType.KINGDOM)
    );
    return kingdoms;
  }
}
