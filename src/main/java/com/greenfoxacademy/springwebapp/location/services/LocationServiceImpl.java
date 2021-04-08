package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import com.greenfoxacademy.springwebapp.location.repositories.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

  private final LocationRepository repo;
  public static final int MAZE_OFFSET_TO_FORM_RECTANGLE = 5;

  //TODO: have to delete if Petr implemented the Shortest Path branch
  @Override
  public Integer distanceBetweenKingdomsWithoutObstacles(KingdomEntity attackingKingdom, KingdomEntity defendingKingdom) {
    return Math.abs(attackingKingdom.getLocation().getX() - defendingKingdom.getLocation().getX())
        + Math.abs(attackingKingdom.getLocation().getY() - defendingKingdom.getLocation().getY());
  }

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
    if (first == null)
      throw new RuntimeException("There is no location to place the kingdom");
    // if range is greater we need to have the loop to check all neighbours in the range
    // otherwise this would just jump over whatever is between Location first and range
    int range = 1;
    for (int i = 1; i <= range; i++) {
      if (!isEligible(kingdoms, first.getX() + i, first.getY()))
        return false;
      else if (!isEligible(kingdoms, first.getX(), first.getY() + i))
        return false;
      else if (!isEligible(kingdoms, first.getX() - i, first.getY()))
        return false;
      else if (!isEligible(kingdoms, first.getX(), first.getY() - i))
        return false;
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

    List<LocationEntity> sortedSmallerRectangleList =
        findAllInRectangleOrdered(MAZE_OFFSET_TO_FORM_RECTANGLE, start.getLocation(), end.getLocation());
    LocationEntity[][] locationMaze = buildMap(sortedSmallerRectangleList);

    return pathFinder(0, start.getLocation(), end.getLocation(),
        locationMaze, sortedSmallerRectangleList);
  }

  public List<LocationEntity> pathFinder(int failSafe, LocationEntity start,
                                         LocationEntity end, LocationEntity[][] maze, List<LocationEntity> locations) {
    Set<LocationEntity> visited = createNewLocationSet();
    PriorityQueue<LocationEntity> toVisit = new PriorityQueue<>(new LocationComparator(end.getX(), end.getY()));
    toVisit.add(start);
    Map<LocationEntity, Integer> distances = prepareDistancesMap(locations, start);
    while (!toVisit.isEmpty() && failSafe < 10000) {
      LocationEntity popped = toVisit.poll();
      visited.add(popped);
      for (LocationEntity neighbor : findNeighbours(popped, maze)) {
        if ((neighbor.getType().equals(LocationType.EMPTY) && !visited.contains(neighbor)) || neighbor.equals(end)) {
          calculateDistanceToStart(neighbor, popped, distances);
          toVisit.add(neighbor);
        }
      }
      if (popped.equals(end)) break;
      failSafe++;
    }
    if (failSafe == 9999) throw new RuntimeException("PATH NOT FOUND");
    return backtrack(distances, end, maze);
  }

  public Map<LocationEntity, Integer> prepareDistancesMap(List<LocationEntity> locations, LocationEntity start) {
    Map<LocationEntity, Integer> distances = createNewHashMap();
    locations.forEach(entry -> distances.put(entry, Integer.MAX_VALUE));
    distances.put(start, 0);
    return distances;
  }

  public List<LocationEntity> backtrack(Map<LocationEntity, Integer> distances,
                                        LocationEntity end, LocationEntity[][] maze) {

    Map<LocationEntity, Integer> distancesWithoutIntMax = removeKeyValuesWithMaxInt(distances);
    List<LocationEntity> reversedPath = createNewLocationArrayList();
    reversedPath.add(end);
    LocationEntity lastAdded = end;
    int distanceOfLastAdded = distancesWithoutIntMax.get(end);
    for (int i = distanceOfLastAdded; i > 0; i--) {
      List<LocationEntity> lastNeighbours = findNeighbours(lastAdded, maze);
      lastAdded = locationWithLowerDistance(lastNeighbours, distancesWithoutIntMax, i - 1);
      reversedPath.add(lastAdded);
    }
    Collections.reverse(reversedPath);
    return reversedPath;
  }

  public Map<LocationEntity, Integer> removeKeyValuesWithMaxInt(Map<LocationEntity, Integer> distances) {
    for (Map.Entry each : distances.entrySet()) {
      if (each.getValue().equals(Integer.MAX_VALUE)) distances.remove(each);
    }
    return distances;
  }

  public LocationEntity locationWithLowerDistance(List<LocationEntity> lastNeighbours,
                                                  Map<LocationEntity, Integer> distances, int distance) {
    return lastNeighbours.stream().filter(current -> distances.get(current) == distance).findFirst().orElse(null);
  }

  public void calculateDistanceToStart(LocationEntity neighbour, LocationEntity popped,
                                       Map<LocationEntity, Integer> distances) {
    if (distances.get(popped) < distances.get(neighbour)) {
      distances.put(neighbour, distances.get(popped) + 1);
    }
  }

  public int[] mapLocationToIndex(LocationEntity popped, LocationEntity start) {
    return new int[]{Math.abs(popped.getY() - start.getY()), Math.abs(popped.getX() - start.getX())};
  }

  public List<LocationEntity> findNeighbours(LocationEntity popped, LocationEntity[][] maze) {
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

  public List<LocationEntity> findAllInRectangleOrdered(
      int mazeOffsetToFormRectangleAroundStartEnd, LocationEntity start, LocationEntity end) {
    int minX = Math.min(end.getX(), start.getX()) - mazeOffsetToFormRectangleAroundStartEnd;
    int maxX = Math.max(end.getX(), start.getX()) + mazeOffsetToFormRectangleAroundStartEnd;
    int minY = Math.min(end.getY(), start.getY()) - mazeOffsetToFormRectangleAroundStartEnd;
    int maxY = Math.max(end.getY(), start.getY()) + mazeOffsetToFormRectangleAroundStartEnd;

    return repo.findAllInRectangleOrdered(minX, maxX, maxY, minY);
  }

  public LocationEntity[][] buildMap(List<LocationEntity> sortReduced) {
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
      map[Math.abs(location.getY() - maxY)][location.getX() - minX] = location;
    }
    return map;
  }

  @AllArgsConstructor
  public static class LocationComparator implements Comparator<LocationEntity> {
    private final int x;
    private final int y;

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

    private int locationDistanceToXY(LocationEntity l2, int x, int y) {
      return Math.abs(l2.getY() - y) + Math.abs(l2.getX() - x);
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