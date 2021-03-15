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
  public LocationEntity assignKingdomLocation(KingdomEntity kingdom) {
    List<LocationEntity> emptyLocations = repo.findAllLocationsByTypeIs(LocationType.EMPTY);
    List<LocationEntity> kingdoms = repo.findAllLocationsByTypeIs(LocationType.KINGDOM);
    if (emptyLocations == null || emptyLocations.size() == 0)
      throw new RuntimeException("There is no location to place the kingdom");
    PriorityQueue<LocationEntity> locationsInQueue = prioritizeLocationsByCoordinates(0, 0, emptyLocations);
    LocationEntity first = locationsInQueue.poll();

    while (!isTypeChangeableToTarget(first, LocationType.KINGDOM, kingdoms)) {
      if (first == null)
        throw new RuntimeException("There is no location to place the kingdom");
      first = locationsInQueue.poll();
    }

    first.setKingdom(kingdom);
    first.setType(LocationType.KINGDOM);
    repo.save(first);

    return first;
  }

  @Override
  public boolean isTypeChangeableToTarget(LocationEntity first, LocationType targetType,
                                          List<LocationEntity> kingdoms) {
    int range = 1;
    if (!isEligible(kingdoms, first.getX() + range, first.getY(), targetType)) {
      return false;
    } else if (!isEligible(kingdoms, first.getX(), first.getY() + range, targetType)) {
      return false;
    } else if (!isEligible(kingdoms, first.getX() - range, first.getY(), targetType)) {
      return false;
    } else if (!isEligible(kingdoms, first.getX(), first.getY() - range, targetType)) {
      return false;
    } else {
      return true;
    }
  }

  public boolean isEligible(List<LocationEntity> kingdoms, int x, int y, LocationType targetType) {
    LocationEntity found = kingdoms
        .stream()
        .filter(each -> each.getX() == x && each.getY() == y)
        .findFirst().orElse(null);
    return found == null || (!found.getType().equals(targetType));
  }


  public PriorityQueue<LocationEntity> prioritizeLocationsByCoordinates(int x, int y, List<LocationEntity> locations) {
    PriorityQueue<LocationEntity> result = new PriorityQueue<>(locations.size(), new LocationComparator(x, y));
    result.addAll(locations);
    return result;
  }

  @AllArgsConstructor
  public static class LocationComparator implements Comparator<LocationEntity> {
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