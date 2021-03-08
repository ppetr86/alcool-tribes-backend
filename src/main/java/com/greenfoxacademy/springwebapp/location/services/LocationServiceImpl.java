package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.Coordinate;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import com.greenfoxacademy.springwebapp.location.repositories.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

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
  public List<Coordinate> findShortestPath(KingdomEntity start, KingdomEntity end) {

    List<Coordinate> result = new ArrayList<>();
    result.add(new Coordinate(start.getLocation().getX(), start.getLocation().getY()));
    result.add(new Coordinate(end.getLocation().getX(), end.getLocation().getY()));

    //Legend: 1: Wall, 0: valid path, s: start, e: end
    //string array of 201x201 because DB locations can be -100+100 inclusive
    String[][] grid = new String[201][201];
    List<LocationEntity> dbLocations = repo.findAll();
    //offset between array and DB location
    //array starts at 0,0 while DB locationEntity starts at -100-100
    int offset = 101;

    // setting all LocationType NOTEMPTY to "1" for wall
    for (LocationEntity each : dbLocations) {
      if (!each.getType().equals(LocationType.EMPTY)) {
        //database grid is from -100 to +100 both inclusive
        int xCoord = each.getX() + offset;
        int yCoord = each.getY() + offset;
        grid[xCoord][yCoord] = "1";
      }
    }

    //setting all null to "0" for valid path
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        if (grid[i][j] == null) {
          grid[i][j] = "0";
        }
      }
    }

    //setting start  location to "s" , end to "e"
    grid[start.getLocation().getY() + offset][start.getLocation().getX() + offset] = "s";
    grid[end.getLocation().getY() + offset][end.getLocation().getX() + offset] = "e";
    // 1: Wall, 0: valid path, s: start, e: end

   /* Stack<Coordinate> path = new Stack<>();
    System.out.println(shortestPath(map, new Coordinate(1, 0), new Coordinate(7, 9), path));

    while (!path.isEmpty()) {
      System.out.print(path.pop() + ", ");*/

    System.out.println(Arrays.deepToString(grid));

    return result;
  }
}

  /*public static int shortestPath(String[][] map, Cell start, Cell end,
                                 Stack<Cell> path) {
    // initialize distances array filled with infinity
    int[][] distances = new int[map.length][];
    for (int i = 0; i < map.length; i++) {
      distances[i] = new int[map[i].length];
      Arrays.fill(distances[i], Integer.MAX_VALUE);
    }

    // the start node should get distance 0
    int distance = 0;
    List<Cell> currentCells = Arrays.asList(start);

    while (distances[end.row][end.col] == Integer.MAX_VALUE
        && !currentCells.isEmpty()) {
      List<Cell> nextCells = new ArrayList<>();

      // loop over all cells added in previous round
      // set their distance
      //    and add their neighbors to the list for next round
      for (Cell cell : currentCells) {
        if (distances[cell.row][cell.col] == Integer.MAX_VALUE
            && !map[cell.row][cell.col].equals("1")) {
          distances[cell.row][cell.col] = distance;
          addNeighbors(cell, nextCells, map.length, map[0].length);
        }
      }

      // prepare for next round
      currentCells = nextCells;
      distance++;
    }

    // find the path
    if (distances[end.row][end.col] < Integer.MAX_VALUE) {
      Cell cell = end;
      path.push(end);
      for (int d = distances[end.row][end.col] - 1; d >= 0; d--) {
        cell = getNeighbor(cell, d, distances);
        path.push(cell);
      }
    }

    return distances[end.row][end.col];
  }*/