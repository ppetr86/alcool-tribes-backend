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
  public List<LocationEntity> findShortestPathV99(KingdomEntity start, KingdomEntity end) {

    List<LocationEntity> dbLocations = repo.findAll();

    int mazeShifter = 2;
    List<LocationEntity> smallerRectangleList = locationsRectangleAroundStartAndEnd(mazeShifter, start.getLocation(), end.getLocation(), dbLocations);
    List<LocationEntity> sortedSmallerRectangleList = sortReducedLocations(smallerRectangleList);
    LocationEntity[][] locationMaze = buildMap(sortedSmallerRectangleList);

    List<LocationEntity> result = pathFinder(start.getLocation(), end.getLocation());

    return result;
  }

  private List<LocationEntity> pathFinder(LocationEntity start, LocationEntity end) {
    boolean shortestPathFound = false;
    Queue<LocationEntity> queue = new LinkedList<>();
    Set<LocationEntity> visitedNodes = new HashSet<>();
    List<LocationEntity> shortestPath = new ArrayList<>();
    queue.add(start);
    shortestPath.add(end);

    while (!queue.isEmpty()) {
      LocationEntity nextNode = queue.peek();
      shortestPathFound = (nextNode.equals(end));
      if (shortestPathFound) break;
      visitedNodes.add(nextNode);
      LocationEntity unvisitedNode = getUnvisitedNode(nextNode, visitedNodes);

      if (unvisitedNode != null) {
        queue.add(unvisitedNode);
        visitedNodes.add(unvisitedNode);
        shortestPath.add(nextNode); //Adding the previous node of the visited node
        shortestPathFound = unvisitedNode.equals(end);
        if (shortestPathFound) break;
      } else {
        queue.poll();
      }
    }
    return shortestPath;
  }

  private LocationEntity[][] buildMap(List<LocationEntity> sortReduced) {
    LocationEntity firstLocation = sortReduced.get(0);
    LocationEntity lastLocation = sortReduced.get(sortReduced.size() - 1);
    int minX = Math.min(firstLocation.getX(), lastLocation.getX());
    int maxY = Math.max(firstLocation.getY(), lastLocation.getY());
    int dimensions = (int) Math.sqrt(sortReduced.size());
    LocationEntity[][] map = new LocationEntity[dimensions][dimensions];
    for (LocationEntity location : sortReduced) {
      map[Math.abs(location.getY() - maxY)][location.getX() - minX] = location;
    }
    return map;

  }

  private List<LocationEntity> sortReducedLocations(List<LocationEntity> reducedLocations) {
    Collections.sort(reducedLocations, new Comparator<LocationEntity>() {
      public int compare(LocationEntity x1, LocationEntity x2) {
        // decreasing order on Y
        int result = Double.compare(x2.getY(), x1.getY());
        if (result == 0) {
          // both Y are equal -> compare X too
          // ascending order on X
          result = Double.compare(x1.getX(), x2.getX());
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