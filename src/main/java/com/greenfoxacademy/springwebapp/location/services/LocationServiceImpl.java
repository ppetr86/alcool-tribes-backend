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

    int mazeOffsetToFormRectangleAroundStartEnd = 2;
    List<LocationEntity> sortedSmallerRectangleList =
        findAllInRectangleOrdered(mazeOffsetToFormRectangleAroundStartEnd, start.getLocation(), end.getLocation());
    LocationEntity[][] locationMaze = buildMap(sortedSmallerRectangleList);
    List<LocationEntity> result =
        pathFinder(start.getLocation(), end.getLocation(), locationMaze, sortedSmallerRectangleList);
    return result;
  }

  private List<LocationEntity> pathFinder(
      LocationEntity start, LocationEntity end, LocationEntity[][] maze, List<LocationEntity> locations) {

    Set<LocationEntity> visited = createNewLocationSet();
    PriorityQueue<LocationEntity> toVisit = new PriorityQueue<>(new LocationComparator(end.getX(), end.getY()));
    toVisit.add(start);
    HashMap<LocationEntity, Integer> distances = createNewHashMap();
    locations.forEach(entry -> distances.put(entry, Integer.MAX_VALUE));
    distances.put(start, 0);

    while (!toVisit.isEmpty()) {
      LocationEntity popped = toVisit.poll();

      visited.add(popped);
      List<LocationEntity> neighbours = findNeighbours(popped, maze);
      for (LocationEntity neighbour : neighbours) {
        if ((neighbour.getType().equals(LocationType.EMPTY) && !visited.contains(neighbour)) || neighbour.equals(end)) {
          calculateDistanceToStart(neighbour, popped, distances);
          toVisit.add(neighbour);
        }
      }
      if (popped.equals(end)) break;
    }
    return backtrack(distances, end, maze);
  }

  private Boolean isNeighbourShorterDistanceToEndThanPopped(LocationEntity popped,
                                                            LocationEntity neighbour, LocationEntity end) {
    return locationDistanceToXY(popped, end) > locationDistanceToXY(neighbour, end);
  }

  private List<LocationEntity> backtrack(HashMap<LocationEntity, Integer> distances, LocationEntity end, LocationEntity[][] maze) {

    List<LocationEntity> reversedPath = createNewLocationArrayList();
    reversedPath.add(end);
    LocationEntity lastAdded = end;
    int distanceOfLastAdded = distances.get(end);
    for (int i = distanceOfLastAdded; i > 0; i--) {
      List<LocationEntity> lastNeighbours = findNeighbours(lastAdded, maze);
      LocationEntity locationWithLowerDistance = locationWithLowerDistance(lastNeighbours, distances, i - 1);
      lastAdded = locationWithLowerDistance;
      reversedPath.add(lastAdded);
    }
    Collections.reverse(reversedPath);
    return reversedPath;
  }

  private LocationEntity locationWithLowerDistance(List<LocationEntity> lastNeighbours, HashMap<LocationEntity, Integer> distances, int distance) {
    return lastNeighbours.stream().filter(current -> distances.get(current) == distance).findFirst().orElse(null);
  }

  private void calculateDistanceToStart(LocationEntity neighbour, LocationEntity popped,
                                        HashMap<LocationEntity, Integer> distances) {
    if (distances.get(popped) < distances.get(neighbour)) {
      distances.put(neighbour, distances.get(popped) + 1);
    }
  }

  private int[] mapLocationToIndex(LocationEntity popped, LocationEntity start) {
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

    return repo.findAllInRectangleOrdered(minX, maxX, maxY, minY);
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

    /*private double locationDistanceToXY(LocationEntity l2, int x, int y) {
      double result = Math.sqrt((Math.pow(l2.getX() - x, 2) + Math.pow(l2.getY() - y, 2)));
      return result;
    }*/

    private int locationDistanceToXY(LocationEntity l2, int x, int y) {
      return Math.abs(l2.getY() - y) + Math.abs(l2.getX() - x);
    }
  }

  public int locationDistanceToXY(LocationEntity l1, LocationEntity l2) {
    return Math.abs(l2.getY() - l1.getY()) + Math.abs(l2.getX() - l1.getX());
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

  private Stack<List<LocationEntity>> createNewStack() {
    return new Stack<>();
  }
}
