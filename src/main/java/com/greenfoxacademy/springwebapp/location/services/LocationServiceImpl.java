package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.Node;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import com.greenfoxacademy.springwebapp.location.repositories.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

@AllArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

  private static final int r[] = {-1, 0, 0, 1};
  private static final int c[] = {0, -1, 1, 0};
  private final LocationRepository repo;

  @Override
  public LocationEntity save(LocationEntity entity) {
    return repo.save(entity);
  }

  @Override
  public LocationEntity defaultLocation(KingdomEntity kingdom) {

    boolean isOccupied = true;
    LocationEntity startingLocation = null;
    List<LocationEntity> allLocations = findAll();

    while (isOccupied) {
      startingLocation = generateRandomLocation();
      if (!allLocations.contains(startingLocation)) {
        isOccupied = false;
      }
    }
    startingLocation.setKingdom(kingdom);
    startingLocation.setType(LocationType.KINGDOM);
    return startingLocation;
  }

  private LocationEntity generateRandomLocation() {
    LocationEntity result = new LocationEntity();
    result.setX(new Random().nextInt(201) - 100);
    result.setY(new Random().nextInt(201) - 100);
    return result;
  }

  @Override
  public List<LocationEntity> findAll() {
    return repo.findAll();
  }

  @Override
  public void generate50DesertsAnd50Jungles() {
    repo.generate50DesertsAnd50Jungles();
  }

  @Override
  public List<Node> findShortestPath(KingdomEntity start, KingdomEntity end) {

    List<Node> result = new ArrayList<>();
    result.add(new Node(start.getLocation().getX(), start.getLocation().getY()));
    result.add(new Node(end.getLocation().getX(), end.getLocation().getY()));

    //Legend: 1: Wall, 0: valid path, 3: start, 4: end
    int[][] grid = new int[201][201];
    Arrays.stream(grid).forEach(a -> Arrays.fill(a, 0));
    List<LocationEntity> dbLocations = repo.findAll();

    // setting all LocationType NOTEMPTY to "1" for wall
    for (LocationEntity each : dbLocations) {
      if (!each.getType().equals(LocationType.EMPTY)) {
        int yCoord = each.getY() * (-1) + 100;
        grid[yCoord][each.getX() + 100] = 1;
      }
    }
    //setting start  location to "s" , end to "e"
    grid[start.getLocation().getY() * (-1) + 100][start.getLocation().getX() + 100] = 3;
    grid[end.getLocation().getY() * (-1) + 100][end.getLocation().getX() + 100] = 4;
    System.out.println(Arrays.deepToString(grid));


    HashMap<Node, Integer> visitedNodes = new HashMap<>();
    HashMap<Node, Integer> currentShortestDistance = new HashMap<>();
    PriorityQueue<Node> queue = new PriorityQueue<>();
    return result;
  }


}