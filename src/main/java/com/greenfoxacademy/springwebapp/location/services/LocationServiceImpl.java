package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import com.greenfoxacademy.springwebapp.location.repositories.LocationRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

  private final LocationRepository repo;

  @Override
  public LocationEntity save(LocationEntity entity) {
    return repo.save(entity);
  }

  @Override
  public LocationEntity defaultLocation(KingdomEntity kingdom) {

    boolean isOccupied = true;
    LocationEntity startingLocation = null;
    List<LocationEntity> allLocations = findAll();

    while (isOccupied) {
      startingLocation = generateRandomLocation();
      if (!allLocations.contains(startingLocation)) {
        isOccupied = false;
      }
    }
    startingLocation.setKingdom(kingdom);
    startingLocation.setType(LocationType.KINGDOM);
    return startingLocation;
  }

  private LocationEntity generateRandomLocation() {
    LocationEntity result = new LocationEntity();
    result.setX(new Random().nextInt(201) - 100);
    result.setY(new Random().nextInt(201) - 100);
    return result;
  }

  @Override
  public List<LocationEntity> findAll() {
    return repo.findAll();
  }

  @Override
  public void generate50DesertsAnd50Jungles() {
    repo.generate50DesertsAnd50Jungles();
  }

  @Override
  public List<LocationEntity> findShortestPath(KingdomEntity start, KingdomEntity destination) {
    int[][] allLocations = new int[100][100];
    List<LocationEntity> dbLocations = repo.findAll();
    for (LocationEntity each : dbLocations) {
      allLocations[each.getY()][each.getX()] = 1;
    }
    // 0 = free|| 1=ocuppied

  }

  @Getter
  @Setter
  @AllArgsConstructor
  static class Coordinate {
    int x;
    int y;
    int distanceFromSource;
  }


}
