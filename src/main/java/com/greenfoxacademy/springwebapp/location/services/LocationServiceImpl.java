package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import com.greenfoxacademy.springwebapp.location.repositories.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

@AllArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

  private final LocationRepository repo;

  // Returns false if not a valid position
  private static boolean isValid(int x, int y) {
    return (x < 201 && y < 201 && x >= 0 && y >= 0);
  }

  //method to determine if the node d is linked to the node s
  public static boolean isLinked(Node s, Node d) {
    if (d.Right == s) return true;
    if (d.Bottom == s) return true;
    if (d.Left == s) return true;
    if (d.Top == s) return true;
    return false;
  }

  public static boolean isValid(int[][] a, int x, int y) {
    if (x >= 0 && x < a.length && y >= 0 && y < a.length)
      return true;
    return false;
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
  public List<Node> findShortestPathV99(KingdomEntity start, KingdomEntity end) {

    List<Node> result = new ArrayList<>();
    result.add(new Node(new int[]{start.getLocation().getX(), start.getLocation().getY()}));
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

    Node[][] net = nodeArray(grid);
    shortestPath(net, 0, 0);
    return result;
  }


  private static class Node {
    private int[] coordinate = new int[2];
    private int data;
    private Node Right, Left, Top, Bottom;

    public Node() {
    }


  }

  public static Node[][] nodeArray(int[][] a) {
    Node[][] nodeA = new Node[a.length][a.length];
    for (int i = 0; i < nodeA.length; i++)
      for (int j = 0; j < nodeA[i].length; j++) {
        nodeA[i][j] = new Node();
        nodeA[i][j].coordinate[0] = i;
        nodeA[i][j].coordinate[1] = j;
        nodeA[i][j].data = a[i][j];
      }
    for (int i = 0; i < nodeA.length; i++)
      for (int j = 0; j < nodeA[i].length; j++) {
        if (isValid(a, i, j + nodeA[i][j].data))
          nodeA[i][j].Right = nodeA[i][j + nodeA[i][j].data];
        if (isValid(a, i, j - nodeA[i][j].data))
          nodeA[i][j].Left = nodeA[i][j - nodeA[i][j].data];
        if (isValid(a, i + nodeA[i][j].data, j))
          nodeA[i][j].Bottom = nodeA[i + nodeA[i][j].data][j];
        if (isValid(a, i - nodeA[i][j].data, j))
          nodeA[i][j].Top = nodeA[i - nodeA[i][j].data][j];
      }
    return nodeA;
  }

  public static void shortestPath(Node[][] nodes, int x, int y) {
    Stack<Node> stack = new Stack<>();
    Queue<Node> queue = new LinkedList<>();
    int[][] path = new int[nodes.length][nodes[0].length];
    boolean b = false;
    int level = 1;//to keep tracking each level viseted
    queue.add(nodes[x][y]);
    path[x][y] = level;
    while (!queue.isEmpty()) {
      Node temp;
      level++;
      int size = queue.size();
      for (int i = 0; i < size; i++) {
        temp = queue.remove();
        if (temp.data == -1) {
          b = true;
          break;
        }
        if (temp.Right != null && path[temp.Right.coordinate[0]][temp.Right.coordinate[1]] == 0) {
          queue.add(temp.Right);
          path[temp.Right.coordinate[0]][temp.Right.coordinate[1]] = level;
        }
        if (temp.Bottom != null && path[temp.Bottom.coordinate[0]][temp.Bottom.coordinate[1]] == 0) {
          queue.add(temp.Bottom);
          path[temp.Bottom.coordinate[0]][temp.Bottom.coordinate[1]] = level;
        }
        if (temp.Left != null && path[temp.Left.coordinate[0]][temp.Left.coordinate[1]] == 0) {
          queue.add(temp.Left);
          path[temp.Left.coordinate[0]][temp.Left.coordinate[1]] = level;
        }
        if (temp.Top != null && path[temp.Top.coordinate[0]][temp.Top.coordinate[1]] == 0) {
          queue.add(temp.Top);
          path[temp.Top.coordinate[0]][temp.Top.coordinate[1]] = level;
        }
      }
      if (b) break;
    }
    if (b) {
      int x1 = 0, y1 = 0;
      for (int i = 0; i < nodes.length; i++)// to locate the position of the goal
        for (int j = 0; j < nodes.length; j++)
          if (nodes[i][j].data == -1) {
            x1 = i;
            y1 = j;
          }
      stack.add(nodes[x1][y1]);
      int d = path[x1][y1];
      while (d > 0)//go back from the goal to the source
      {
        for (int i = 0; i < path.length; i++) {
          if (path[x1][i] == d - 1 && isLinked(nodes[x1][y1], nodes[x1][i])) {
            stack.add(nodes[x1][i]);
            y1 = i;
            break;
          } else if (path[i][y1] == d - 1 && isLinked(nodes[x1][y1], nodes[i][y1])) {
            stack.add(nodes[i][y1]);
            x1 = i;
            break;
          }
        }
        d--;
      }
      Node temp;
      int stackSize = stack.size();
      for (int i = 0; i < stackSize; i++)// print the final result
      {
        temp = stack.pop();
        System.out.print("(" + temp.coordinate[0] + " " + temp.coordinate[1] + ") ");
      }
    } else System.out.print("No Solution Possible.");
  }
}