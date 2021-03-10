package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.Maze;
import com.greenfoxacademy.springwebapp.location.models.Coordinate;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import com.greenfoxacademy.springwebapp.location.repositories.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

  private final LocationRepository repo;
  private static int[][] DIRECTIONS
      = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

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
  public List<Coordinate> findShortestPathV99(KingdomEntity start, KingdomEntity end) {

    List<Coordinate> result = new ArrayList<>();

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

    Maze maze = new Maze(grid);
    List<Coordinate> path = solve(maze);
    maze.printPath(path);
    maze.reset();

    return result;
  }

  public List<Coordinate> solve(Maze maze) {
    LinkedList<Coordinate> nextToVisit = new LinkedList<>();
    Coordinate start = maze.getEntry();
    nextToVisit.add(start);

    while (!nextToVisit.isEmpty()) {
      Coordinate cur = nextToVisit.remove();

      if (!maze.isValidLocation(cur.getX(), cur.getY()) || maze.isExplored(cur.getX(), cur.getY())) {
        continue;
      }

      if (maze.isWall(cur.getX(), cur.getY())) {
        maze.setVisited(cur.getX(), cur.getY(), true);
        continue;
      }

      if (maze.isExit(cur.getX(), cur.getY())) {
        return backtrackPath(cur);
      }

      for (int[] direction : DIRECTIONS) {
        Coordinate coordinate = new Coordinate(cur.getX() + direction[0], cur.getY() + direction[1], cur);
        nextToVisit.add(coordinate);
        maze.setVisited(cur.getX(), cur.getY(), true);
      }
    }
    return Collections.emptyList();
  }

  private List<Coordinate> backtrackPath(Coordinate cur) {
    List<Coordinate> path = new ArrayList<>();
    Coordinate iter = cur;

    while (iter != null) {
      path.add(iter);
      iter = iter.getParent();
    }

    return path;
  }
}