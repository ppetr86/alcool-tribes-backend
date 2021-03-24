package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.Maze;
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
  private final PathFinder pathFinder;

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

    //Legend: 1: Wall, 0: valid path, 3: start, 4: end
    int[][] grid = new int[201][201];
    List<LocationEntity> dbLocations = repo.findAll();

    // setting all LocationType NOTEMPTY to "1" for can not walk
    for (LocationEntity each : dbLocations) {
      if (!each.getType().equals(LocationType.EMPTY)) {
        int yCoord = each.getY() * (-1) + 100;
        grid[yCoord][each.getX() + 100] = 1;
      }
    }
    //setting start  location to "3" , end to "4"
    grid[start.getLocation().getY() * (-1) + 100][start.getLocation().getX() + 100] = 3;
    grid[end.getLocation().getY() * (-1) + 100][end.getLocation().getX() + 100] = 4;
//    System.out.println(Arrays.deepToString(grid));

    Maze maze = new Maze(grid);
    maze.setStart(new Coordinate(start.getLocation().getY(),start.getLocation().getX()));
    maze.setEnd(new Coordinate(end.getLocation().getY(),end.getLocation().getX()));
    List<Coordinate> path = pathFinder.solve(maze);
    maze.reset();
    return path;
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