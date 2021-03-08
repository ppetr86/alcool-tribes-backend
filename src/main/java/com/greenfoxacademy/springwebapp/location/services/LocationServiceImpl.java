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
import java.util.Stack;

@AllArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

  private final LocationRepository repo;

  public static int shortestPath(String[][] grid, Coordinate start, Coordinate end,
                                 Stack<Coordinate> path) {
    // initialize distances array filled with infinity
    int[][] distances = new int[grid.length][];

    for (int i = 0; i < grid.length; i++) {
      distances[i] = new int[grid[i].length];
      Arrays.fill(distances[i], Integer.MAX_VALUE);
    }

    // the start node should get distance 0
    int distance = 0;
    List<Coordinate> currentCells = Arrays.asList(start);

    while (distances[end.getRow()][end.getCol()] == Integer.MAX_VALUE
        && !currentCells.isEmpty()) {
      List<Coordinate> nextCells = new ArrayList<>();

      // loop over all cells added in previous round
      // set their distance
      //    and add their neighbors to the list for next round
      for (Coordinate cell : currentCells) {
        if (distances[cell.getRow()][cell.getCol()] == Integer.MAX_VALUE
            && !grid[cell.getRow()][cell.getCol()].equals("1")) {
          distances[cell.getRow()][cell.getCol()] = distance;
          addNeighbors(cell, nextCells, grid.length, grid[0].length);
        }
      }

      // prepare for next round
      currentCells = nextCells;
      distance++;
    }

    // find the path
    if (distances[end.getRow()][end.getCol()] < Integer.MAX_VALUE) {
      Coordinate cell = end;
      path.push(end);
      for (int d = distances[end.getRow()][end.getCol()] - 1; d >= 0; d--) {
        cell = getNeighbor(cell, d, distances);
        path.push(cell);
      }
    }

    return distances[end.getRow()][end.getCol()];
  }

  // add all valid neighbors of a cell to the list
  // where "valid" means: indices inside the maze
  private static void addNeighbors(Coordinate cell, List<Coordinate> list,
                                   int maxRow, int maxCol) {
    int[][] ds = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    for (int[] d : ds) {
      int row = cell.getRow() + d[0];
      int col = cell.getCol() + d[1];
      if (isValid(row, col, maxRow, maxCol)) {
        list.add(new Coordinate(row, col));
      }
    }
  }

  // find the neighbor of a cell having a certain distance from the start
  private static Coordinate getNeighbor(Coordinate cell, int distance, int[][] distances) {
    int[][] ds = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    for (int[] d : ds) {
      int row = cell.getRow() + d[0];
      int col = cell.getCol() + d[1];
      if (isValid(row, col, distances.length, distances[0].length)
          && distances[row][col] == distance) {
        return new Coordinate(row, col);
      }
    }
    return null;
  }

  // check if coordinates are inside the maze
  private static boolean isValid(int row, int col, int maxRow, int maxCol) {
    return row >= 0 && row < maxRow && col >= 0 && col < maxCol;
  }

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
    String[][] grid = new String[201][201];
    Arrays.stream(grid).forEach(a -> Arrays.fill(a, "0"));
    List<LocationEntity> dbLocations = repo.findAll();

    // setting all LocationType NOTEMPTY to "1" for wall
    for (LocationEntity each : dbLocations) {
      if (!each.getType().equals(LocationType.EMPTY)) {
        int yCoord = each.getY() * (-1) + 100;
        grid[yCoord][each.getX() + 100] = "1";
      }
    }
    //setting start  location to "s" , end to "e"
    grid[start.getLocation().getY() * (-1) + 100][start.getLocation().getX() + 100] = "S";
    grid[end.getLocation().getY() * (-1) + 100][end.getLocation().getX() + 100] = "E";
    System.out.println(Arrays.deepToString(grid));

    Stack<Coordinate> path = new Stack<>();

    Coordinate startCoord = new Coordinate(start.getLocation().getY() * (-1) + 100, start.getLocation().getX() + 100);
    Coordinate endCoord = new Coordinate(end.getLocation().getY() * (-1) + 100, end.getLocation().getX() + 100);

    shortestPath(grid, startCoord, endCoord, path);

    while (!path.isEmpty()) {
      Coordinate temp = path.pop();
      temp.setCol(temp.getCol()* (-1) + 100);
      temp.setRow(temp.getRow()-100);
      result.add(temp);
    }
    return result;
  }

  @Override
  public List<LocationEntity> findAllInRange(KingdomEntity start, KingdomEntity end, int offset) {
    int smallerX = start.getLocation().getX();
    int biggerX = end.getLocation().getX();
    int smallerY = start.getLocation().getY();
    int biggerY = end.getLocation().getY();

    if (start.getLocation().getX() > end.getLocation().getX()) {
      smallerX = end.getLocation().getX();
      biggerX = start.getLocation().getX();
    }

    if (start.getLocation().getY() > end.getLocation().getY()) {
      smallerY = end.getLocation().getY();
      biggerY = start.getLocation().getY();
    }
    return repo.findAll();
  }
}