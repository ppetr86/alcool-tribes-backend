package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationDistance;
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
  public List<LocationEntity> findShortestPath(KingdomEntity start, KingdomEntity end) {

    int mazeOffsetToFormRectangleAroundStartEnd = 2;
    List<LocationEntity> sortedSmallerRectangleList = findAllInRectangleOrdered(mazeOffsetToFormRectangleAroundStartEnd, start.getLocation(), end.getLocation());
    LocationEntity[][] locationMaze = buildMap(sortedSmallerRectangleList);

    List<LocationEntity> result = pathFinder(mazeOffsetToFormRectangleAroundStartEnd, start.getLocation(), end.getLocation(), locationMaze, sortedSmallerRectangleList);
    return result;
  }

  private List<LocationEntity> pathFinder(int offset, LocationEntity start, LocationEntity end, LocationEntity[][] maze, List<LocationEntity> locations) {
    // create `previousNodes` collection
    // put start in `toVisit`
    // fill distances in `distancesFromStart` with highest value for all nodes except `start` which should be 0

    // while we haven't reach the destination and `toVisit` is not empty
    // `currentNode` should be the closest element from `toVisit`
    // put those neighbours of `currentNode` in `toVisit` which we haven't visited yet
    // for each neighbour check if the currently known distance from start is higher than the distance from start to current + current to neighbour. if yes, update `distancesFromStart` with the new value and update `previousNodes` with current
    // if 'previousNodes` doesn't contain the neighbour, then add it with the source of current

    // track back the shortest path from `previousNodes` starting from `destination` key till `start` key
    // return the path
    List<LocationEntity> shortestPath = new ArrayList<>();
    Set<LocationEntity> visited = new HashSet<>();
    PriorityQueue<LocationEntity> toVisit = new PriorityQueue<>();
    toVisit.add(start);
    visited.add(start);
    int[][] distancesFromStart = new int[maze.length][maze[0].length];

    for (int[] ints : distancesFromStart) {
      Arrays.fill(ints, Integer.MAX_VALUE);
    }
    distancesFromStart[offset][offset] = 0;




  }

  private List<LocationEntity> findAllInRectangleOrdered(int mazeOffsetToFormRectangleAroundStartEnd, LocationEntity start, LocationEntity end) {
    int minX = Math.min(end.getX(), start.getX()) - mazeOffsetToFormRectangleAroundStartEnd;
    int maxX = Math.max(end.getX(), start.getX()) + mazeOffsetToFormRectangleAroundStartEnd;
    int minY = Math.min(end.getY(), start.getY()) - mazeOffsetToFormRectangleAroundStartEnd;
    int maxY = Math.max(end.getY(), start.getY()) + mazeOffsetToFormRectangleAroundStartEnd;

    List<LocationEntity> result = repo.findAllInRectangleOrdered(minX, maxX, maxY, minY);
    return result;
  }




  private LocationEntity[][] buildMap(List<LocationEntity> sortReduced) {
    LocationEntity firstLocation = sortReduced.get(0);
    LocationEntity lastLocation = sortReduced.get(sortReduced.size() - 1);
    int minX = Math.min(firstLocation.getX(), lastLocation.getX());
    int maxX = Math.max(firstLocation.getX(), lastLocation.getX());
    int minY = Math.min(firstLocation.getY(), lastLocation.getY());
    int maxY = Math.max(firstLocation.getY(), lastLocation.getY());
    int width = maxX - minX + 1;
    int height = maxY - minY + 1;
    LocationEntity[][] map = new LocationEntity[width][height];
    for (LocationEntity location : sortReduced) {
      map[Math.abs(location.getY() - maxY)][location.getX() - minX] = location;
    }
    return map;
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
