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
    Set<LocationEntity> kingdoms = allLocations.stream()
        .filter(x -> x.getType().equals(LocationType.KINGDOM))
        .collect(Collectors.toSet());
    List<LocationEntity> emptyLocations = allLocations.stream()
        .filter(x -> x.getType().equals(LocationType.EMPTY))
        .collect(Collectors.toList());
    PriorityQueue<LocationEntity> locationsInQueue = prioritizeLocationsByCoordinates(0, 0, emptyLocations);
    LocationEntity popped = locationsInQueue.poll();

    while (!isTypeChangeableToTarget(popped, kingdoms)) {
      popped = locationsInQueue.poll();
    }

    popped.setKingdom(kingdom);
    popped.setType(LocationType.KINGDOM);
    return popped;
  }

  @Override
  public boolean isTypeChangeableToTarget(LocationEntity first, Set<LocationEntity> kingdoms) {

    if (first == null) {
      throw new RuntimeException("There is no location to place the kingdom");
    }
    // if range is greater we need to have the loop to check all neighbours in the range
    // otherwise this would just jump over whatever is between Location first and range
    int range = 1;
    for (int i = 1; i <= range; i++) {
      if (!isEligible(kingdoms, first.getX() + i, first.getY())) {
        return false;
      } else if (!isEligible(kingdoms, first.getX(), first.getY() + i)) {
        return false;
      } else if (!isEligible(kingdoms, first.getX() - i, first.getY())) {
        return false;
      } else if (!isEligible(kingdoms, first.getX(), first.getY() - i)) {
        return false;
      }
    }
    return true;
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

    int mazeOffsetToFormRectangleAroundStartEnd = 1;
    List<LocationEntity> sortedSmallerRectangleList =
        findAllInRectangleOrdered(mazeOffsetToFormRectangleAroundStartEnd, start.getLocation(), end.getLocation());
    LocationEntity[][] locationMaze = buildMap(sortedSmallerRectangleList);
    List<LocationEntity> result =
        pathFinder(start.getLocation(), end.getLocation(), locationMaze, sortedSmallerRectangleList);
    return result;
  }

  private List<LocationEntity> pathFinder(
      LocationEntity start, LocationEntity end, LocationEntity[][] maze, List<LocationEntity> locations) {

    List<LocationEntity> shortestPath = createNewLocationArrayList();
    Set<LocationEntity> visited = createNewLocationSet();
    //???? I will not visit not-walkable locations desert and jungle ????
    visited.addAll(allDesertsJunglesKingdomsExceptStartEnd(locations, start, end));
    PriorityQueue<LocationEntity> toVisit = new PriorityQueue<>(new LocationComparator(start.getX(), start.getY()));
    toVisit.add(start);
    HashMap<LocationEntity, Integer> distances = createNewHashMap();
    locations.forEach(entry -> distances.put(entry, Integer.MAX_VALUE));
    distances.put(start, 0);

    while (toVisit.isEmpty() == false) {
      LocationEntity popped = toVisit.poll();
      visited.add(popped);
      for (LocationEntity neighbour : findNeighbours(popped, maze)) {
        if ((neighbour.getType().equals(LocationType.EMPTY) && visited.contains(neighbour) == false) || neighbour.equals(end)) {
          calculateDistanceToStart(neighbour, popped, distances);
          toVisit.add(neighbour);
        }
      }
      if (popped.equals(end)) break;
    }
    return shortestPath;
  }

  private List<LocationEntity> allDesertsJunglesKingdomsExceptStartEnd(List<LocationEntity> locations, LocationEntity start, LocationEntity end) {
    /*return locations.stream()
        .filter(location -> !location.getType().equals(LocationType.EMPTY)
            || !location.equals(start)
            || !location.equals(end))
        .collect(Collectors.toList());*/

    List<LocationEntity> result = locations.stream()
        .filter(location -> location.getType().equals(LocationType.DESERT)
            || location.getType().equals(LocationType.JUNGLE)
            || location.getType().equals(LocationType.KINGDOM))
        .collect(Collectors.toList());
    result.removeAll(Arrays.asList(start,end));
    return result;
  }

  private void calculateDistanceToStart(
      LocationEntity neighbour, LocationEntity popped, HashMap<LocationEntity, Integer> distances) {
    // how to do this? get the first in queue and make distance++??
    int qDistance = distances.get(popped);
    int neighbourDistance = distances.get(neighbour);
    if (distances.get(popped) < distances.get(neighbour)) {
      distances.put(neighbour, distances.get(popped) + 1);
    }
  }

  private int[] mapLocationToIndex(LocationEntity popped, LocationEntity start) {
    int[] result = new int[]{Math.abs(popped.getY() - start.getY()), Math.abs(popped.getX() - start.getX())};
    return new int[]{Math.abs(popped.getY() - start.getY()), Math.abs(popped.getX() - start.getX())};
  }

  private List<LocationEntity> findNeighbours(LocationEntity popped, LocationEntity[][] maze) {
    List<LocationEntity> neighbours = new ArrayList<>();
    int[] indexes = mapLocationToIndex(popped, maze[0][0]);
    int x = indexes[1];
    int y = indexes[0];
    if (y - 1 >= 0) neighbours.add(maze[y - 1][x]);
    if (y + 1 < maze.length) neighbours.add(maze[y + 1][x]);
    if (x - 1 >= 0) neighbours.add(maze[y][x - 1]);
    if (x + 1 < maze[y].length) neighbours.add(maze[y][x + 1]);
    return neighbours;
  }


  private List<LocationEntity> findAllInRectangleOrdered(
      int mazeOffsetToFormRectangleAroundStartEnd, LocationEntity start, LocationEntity end) {
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
      double maxDist1 = locationDistanceToXY(l1, this.x, this.y);
      double maxDist2 = locationDistanceToXY(l2, this.x, this.y);

      if (maxDist1 > maxDist2) {
        return 1;
      } else if (maxDist1 < maxDist2) {
        return -1;
      }
      return 0;
    }

    /*private double locationDistanceToXY(LocationEntity l2, int x, int y) {
      double result = Math.sqrt((Math.pow(l2.getX() - x, 2) + Math.pow(l2.getY() - y, 2)));
      return result;
    }*/

    private int locationDistanceToXY(LocationEntity l2, int x, int y) {
      int result = Math.abs(l2.getY() - y) + Math.abs(l2.getX() - x);
      return result;
    }
  }

  public HashMap<LocationEntity, Integer> createNewHashMap() {
    return new HashMap<>();
  }

  public ArrayList<LocationEntity> createNewLocationArrayList() {
    return new ArrayList<>();
  }

  public Set<LocationEntity> createNewLocationSet() {
    return new HashSet<>();
  }
}
