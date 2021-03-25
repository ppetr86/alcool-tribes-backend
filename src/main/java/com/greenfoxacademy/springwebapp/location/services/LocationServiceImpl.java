package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.Coordinate;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import com.greenfoxacademy.springwebapp.location.repositories.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    List<LocationEntity> allLocations = repo.findAll();
    Set<LocationEntity> kingdoms = allLocations.stream().filter(x -> x.getType().equals(LocationType.KINGDOM)).collect(
        Collectors.toSet());
    List<LocationEntity> emptyLocations =
        allLocations.stream().filter(x -> x.getType().equals(LocationType.EMPTY)).collect(
            Collectors.toList());
    PriorityQueue<LocationEntity> locationsInQueue = prioritizeLocationsByCoordinates(0, 0, emptyLocations);
    LocationEntity first = locationsInQueue.poll();

    while (!isTypeChangeableToTarget(first, kingdoms)) {
      first = locationsInQueue.poll();
    }

    first.setKingdom(kingdom);
    first.setType(LocationType.KINGDOM);
    return first;
  }

  @Override
  public boolean isTypeChangeableToTarget(LocationEntity first, Set<LocationEntity> kingdoms) {

    if (first == null) {
      throw new RuntimeException("There is no location to place the kingdom");
    }
    int range = 1;
    if (!isEligible(kingdoms, first.getX() + range, first.getY())) {
      return false;
    } else if (!isEligible(kingdoms, first.getX(), first.getY() + range)) {
      return false;
    } else if (!isEligible(kingdoms, first.getX() - range, first.getY())) {
      return false;
    } else if (!isEligible(kingdoms, first.getX(), first.getY() - range)) {
      return false;
    } else {
      return true;
    }
  }

  public boolean isEligible(Set<LocationEntity> kingdoms, int x, int y) {
    return !kingdoms.contains(new LocationEntity(x, y));
  }

  public PriorityQueue<LocationEntity> prioritizeLocationsByCoordinates(int x, int y, List<LocationEntity> locations) {
    PriorityQueue<LocationEntity> result = new PriorityQueue<>(locations.size(), new LocationComparator(x, y));
    result.addAll(locations);
    return result;
  }

  @Override
  public List<Coordinate> findShortestPathV99(KingdomEntity start, KingdomEntity end) {

    List<Coordinate> result = new ArrayList<>();
    List<LocationEntity> dbLocations = repo.findAll();
    //Legend: 1: Wall, 0: valid path, 3: start, 4: end

    int mazeShifter = 2;
    List<LocationEntity> reducedLocations = locationsRectangleAroundStartAndEnd(mazeShifter, start.getLocation(), end.getLocation(), dbLocations);
    List<LocationEntity> sortReduced = sortReducedLocations(reducedLocations);
    int mazeDimensions = (int) Math.sqrt(reducedLocations.size());
    int[][] grid = new int[mazeDimensions][mazeDimensions];

    // setting all LocationType NOTEMPTY to "1" for can not walk
    for (LocationEntity each : reducedLocations) {
      int yCoord = each.getY() * (-1) + mazeDimensions;

      if (!each.getType().equals(LocationType.EMPTY)) {
        grid[yCoord][each.getX() + mazeDimensions] = 1;
      }
      if (each.equals(start.getLocation())) {
        grid[yCoord][each.getX() + mazeDimensions] = 3;
      }
      if (each.equals(end.getLocation())) {
        grid[yCoord][each.getX() + mazeDimensions] = 4;
      }
    }

    return result;
  }

  private List<LocationEntity> sortReducedLocations(List<LocationEntity> reducedLocations) {
    Collections.sort( reducedLocations, new Comparator<LocationEntity>() {
      public int compare(LocationEntity x1, LocationEntity x2) {
        int result = Double.compare(x1.getX(), x2.getX());
        if ( result == 0 ) {
          // both X are equal -> compare Y too
          result = Double.compare(x1.getY(), x2.getY());
        }
        return result;
      }
    });
    return reducedLocations;
  }

  private List<LocationEntity> locationsRectangleAroundStartAndEnd(int mazeShifter, LocationEntity start, LocationEntity end, List<LocationEntity> dbLocations) {

    int minX = Math.min(end.getX(), start.getX()) - mazeShifter;
    int maxX = Math.max(end.getX(), start.getX()) + mazeShifter;
    int minY = Math.min(end.getY(), start.getY()) - mazeShifter;
    int maxY = Math.max(end.getY(), start.getY()) + mazeShifter;
    List<LocationEntity> result = dbLocations
        .stream()
        .filter(x -> x.getX() >= minX
            && x.getX() <= maxX
            && x.getY() >= minY
            && x.getY() <= maxY
            && x.getY() >= minY)
        .collect(Collectors.toList());

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