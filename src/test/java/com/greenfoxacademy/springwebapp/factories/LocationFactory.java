package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;

import java.util.ArrayList;
import java.util.List;

public class LocationFactory {

  public static List<LocationEntity> createOccupiedLocations() {
    List<LocationEntity> locationEntities = new ArrayList<>();
    int rows = 21;
    int cols = 21;
    int id = 0;

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        locationEntities.add(new LocationEntity((long) id + 1, j - cols / 2, i - rows / 2, null, LocationType.EMPTY));
        id++;
      }
    }
    return locationEntities;
  }
}
