package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
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
    Set<LocationEntity> kingdoms = allLocations.stream().filter(x -> x.getType().equals(LocationType.KINGDOM))
        .collect(Collectors.toSet());
    List<LocationEntity> emptyLocations =
        allLocations.stream().filter(x -> x.getType().equals(LocationType.EMPTY))
            .collect(Collectors.toList());
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
    List<LocationEntity> result = pathFinder(start.getLocation(), end.getLocation(), locationMaze, sortedSmallerRectangleList);
    return result;
  }

  private List<LocationEntity> pathFinder(LocationEntity start, LocationEntity end, LocationEntity[][] maze, List<LocationEntity> locations) {

    List<LocationEntity> shortestPath = new ArrayList<>();
    Set<LocationEntity> visited = new HashSet<>();
    PriorityQueue<LocationEntity> toVisit = new PriorityQueue<>(new LocationComparator(start.getX(), start.getY()));
    toVisit.add(start);
    visited.add(start);
    HashMap<LocationEntity, Integer> distances = new HashMap<>();
    for (int i = 0; i < locations.size(); i++) {
      distances.put(locations.get(i), Integer.MAX_VALUE);
    }
    distances.put(start, 0);

    while (toVisit.isEmpty() == false) {
      //get first from queue
      LocationEntity popped = toVisit.poll();
      //find neighbours of first
      List<LocationEntity> neighboursOfCurrent = findNeighbours(popped, maze);
      for (LocationEntity each : neighboursOfCurrent) {
        // if neighbour is EMPTY or END calculate distance
        if (each.equals(end)) {
          calculateDistanceToStart(each, popped, distances);
          toVisit.add(each);
          break;
        } else if (each.getType().equals(LocationType.EMPTY)) {
          calculateDistanceToStart(each, popped, distances);
          toVisit.add(each);
        }
      }
      if (popped.equals(end)) break;
    }
    
    return shortestPath;
  }

  private void calculateDistanceToStart(LocationEntity firstInQueue, LocationEntity current, HashMap<LocationEntity, Integer> distances) {

    // how to do this? get the first in queue and make distance++??
    if (distances.get(firstInQueue) < distances.get(current)){
      distances.put(current, distances.get(firstInQueue)+1);
    }
  }

  private int findNeighbourShortestDistance(List<LocationEntity> neighbours, HashMap<LocationEntity, Integer> distances) {

    int minDistance = Integer.MAX_VALUE;
    for (LocationEntity each : neighbours) {
      int currentDistance = distances.get(each);
      if (currentDistance < minDistance) {
        minDistance = distances.get(each);
      }
    }
    return minDistance;
  }

  private int[] mapLocationToIndex(LocationEntity firstInQueue, LocationEntity start) {
    int[] result = new int[]{Math.abs(firstInQueue.getY() - start.getY()), Math.abs(firstInQueue.getX() - start.getX())};
    return new int[]{Math.abs(firstInQueue.getY() - start.getY()), Math.abs(firstInQueue.getX() - start.getX())};
  }

  private List<LocationEntity> findNeighbours(LocationEntity firstInQueue, LocationEntity[][] maze) {
    List<LocationEntity> neighbours = new ArrayList<>();
    int[] indexes = mapLocationToIndex(firstInQueue, maze[0][0]);
    int x = indexes[1];
    int y = indexes[0];
    if (y - 1 >= 0) neighbours.add(maze[y - 1][x]);
    if (y + 1 < maze.length) neighbours.add(maze[y + 1][x]);
    if (x - 1 >= 0) neighbours.add(maze[y][x - 1]);
    if (x + 1 < maze[y].length) neighbours.add(maze[y][x + 1]);
    return neighbours;
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
    LocationEntity[][] map = new LocationEntity[height][width];
    for (LocationEntity location : sortReduced) {
      int y = location.getY() - maxY;
      int x = location.getX() - minX;
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
