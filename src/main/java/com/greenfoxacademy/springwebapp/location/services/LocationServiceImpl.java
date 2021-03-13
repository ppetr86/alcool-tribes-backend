package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import com.greenfoxacademy.springwebapp.location.repositories.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

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

    List<LocationEntity> thisTypeLocations = repo.findAllLocationsByTypeIs(LocationType.EMPTY);

    PriorityQueue<LocationEntity> locationsInQueue = prioritizeLocationsByCoordinates(0, 0, thisTypeLocations);
    LocationEntity firstInQueue = locationsInQueue.poll();

    while (hasNeighbourOfType(firstInQueue, LocationType.KINGDOM)) {
      firstInQueue = locationsInQueue.poll();
    }
    firstInQueue.setKingdom(kingdom);
    firstInQueue.setType(LocationType.KINGDOM);
    repo.save(firstInQueue);

    return firstInQueue;
  }

  private boolean hasNeighbourOfType(LocationEntity firstInQueue, LocationType targetType) {
    if (firstInQueue == null) {
      return true;
    }
    LocationEntity compareThisWithFirstInQueue = new LocationEntity(targetType);
    int range = 1;

    // make it 8 to check diagonally not adjacent to another kingdom too??
    for (int i = 0; i < 8; i++) {
      if (i == 0) compareThisWithFirstInQueue = repo.findByXIsAndYIs(firstInQueue.getX() - range, firstInQueue.getY());
      if (i == 1) compareThisWithFirstInQueue = repo.findByXIsAndYIs(firstInQueue.getX() + range, firstInQueue.getY());
      if (i == 2) compareThisWithFirstInQueue = repo.findByXIsAndYIs(firstInQueue.getX(), firstInQueue.getY() - range);
      if (i == 3) compareThisWithFirstInQueue = repo.findByXIsAndYIs(firstInQueue.getX(), firstInQueue.getY() + range);
      //
      if (i == 4)
        compareThisWithFirstInQueue = repo.findByXIsAndYIs(firstInQueue.getX() + range, firstInQueue.getY()-range);
      if (i == 5)
        compareThisWithFirstInQueue = repo.findByXIsAndYIs(firstInQueue.getX() + range, firstInQueue.getY()+range);
      if (i == 6)
        compareThisWithFirstInQueue = repo.findByXIsAndYIs(firstInQueue.getX()-range, firstInQueue.getY() - range);
      if (i == 7)
        compareThisWithFirstInQueue = repo.findByXIsAndYIs(firstInQueue.getX()-range, firstInQueue.getY() + range);
    }
    return compareThisWithFirstInQueue.getType().equals(targetType);
  }

  private PriorityQueue<LocationEntity> prioritizeLocationsByCoordinates(int x, int y, List<LocationEntity> locations) {
    PriorityQueue<LocationEntity> result = new PriorityQueue<>(locations.size(), new LocationComparator(x, y));
    result.addAll(locations);
    return result;
  }

  @AllArgsConstructor
  static class LocationComparator implements Comparator<LocationEntity> {
    private int x;
    private int y;

    public int compare(LocationEntity l1, LocationEntity l2) {
      double maxDist1 = distanceTo(l1, this.x, this.y);
      double maxDist2 = distanceTo(l2, this.x, this.y);

      if (maxDist1 > maxDist2) {
        return 1;
      } else if (maxDist1 < maxDist2) {
        return -1;
      }
      return 0;
    }

    private double distanceTo(LocationEntity l2, int x, int y) {
      double result = Math.sqrt((Math.pow(l2.getX() - x, 2) + Math.pow(l2.getY() - y, 2)));
      return result;
    }
  }
}