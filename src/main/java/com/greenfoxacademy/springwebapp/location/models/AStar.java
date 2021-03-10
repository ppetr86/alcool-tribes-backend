package com.greenfoxacademy.springwebapp.location.models;

import java.util.Arrays;
import java.util.PriorityQueue;

public class AStar {

  public static double aStar(int[][] graph, double[][] heuristic, int start, int goal) {

    //This contains the distances from the start node to all other nodes
    int[] distances = new int[graph.length];
    //Initializing with a distance of "Infinity"
    Arrays.fill(distances, Integer.MAX_VALUE);
    //The distance from the start node to itself is of course 0
    distances[start] = 0;

    //This contains the priorities with which to visit the nodes, calculated using the heuristic.
    double[] priorities = new double[graph.length];
    //Initializing with a priority of "Infinity"
    Arrays.fill(priorities, Integer.MAX_VALUE);
    //start node has a priority equal to straight line distance to goal. It will be the first to be expanded.
    priorities[start] = heuristic[start][goal];

    //This contains whether a node was already visited
    boolean[] visited = new boolean[graph.length];

    //While there are nodes left to visit...
    while (true) {

      // ... find the node with the currently lowest priority...
      double lowestPriority = Integer.MAX_VALUE;
      int lowestPriorityIndex = -1;
      for (int i = 0; i < priorities.length; i++) {
        //... by going through all nodes that haven't been visited yet
        if (priorities[i] < lowestPriority && !visited[i]) {
          lowestPriority = priorities[i];
          lowestPriorityIndex = i;
        }
      }

      if (lowestPriorityIndex == -1) {
        // There was no node not yet visited --> Node not found
        return -1;
      } else if (lowestPriorityIndex == goal) {
        // Goal node found
        System.out.println("Goal node found!");
        return distances[lowestPriorityIndex];
      }


      //...then, for all neighboring nodes that haven't been visited yet....
      for (int i = 0; i < graph[lowestPriorityIndex].length; i++) {
        if (graph[lowestPriorityIndex][i] != 0 && !visited[i]) {
          //...if the path over this edge is shorter...
          if (distances[lowestPriorityIndex] + graph[lowestPriorityIndex][i] < distances[i]) {
            //...save this path as new shortest path
            distances[i] = distances[lowestPriorityIndex] + graph[lowestPriorityIndex][i];
            //...and set the priority with which we should continue with this node
            priorities[i] = distances[i] + heuristic[i][goal];
          }
        }
      }
      visited[lowestPriorityIndex] = true;
    }
  }
}
  /*public static final int VHCOST = 1;
  private Node[][] grid;
  //priority que for open cells
  //open cells: set of nodes to be evaluated
  //put cells with lowest cost in first

  private PriorityQueue<Node> openCells;
  private boolean[][] closedCells;
  private int startRow, startCol;
  private int endRow, endCol;

  public AStar(int width, int height, int si, int sj, int ei, int ej, int[][] blocks) {
    grid = new Node[width][height];
    closedCells = new boolean[width][height];
    openCells = new PriorityQueue<>((Node n1, Node n2) -> {
      return n1.finalCost < n2.finalCost ? -1 : n1.finalCost > n2.finalCost ? 1 : 0;
    });

    startNode(si, sj);
    endNode(ei, ej);

    // initi heuristic and cells
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        grid[i][j] = new Node(i, j);
        grid[i][j].heuristicCost = Math.abs(i - endRow) + Math.abs(j - endCol);
      }
    }

    grid[startRow][startCol].finalCost = 0;

    //we puth the blocks on the grid

    for (int i = 0; i < blocks.length; i++) {
      addBlockOnCell(blocks[i][0], blocks[i][1]);
    }
  }

  public void addBlockOnCell(int i, int j) {
    grid[i][j] = null;
  }

  public void startCell(int i, int j) {
    startRow = i;
    startCol = j;
  }

  public void updateCostIfNeeded(Node current, Node t, int cost) {
    if (t == null || closedCells[t.row][t.col]) {
      return;
    }

    int tFinalCost = t.heuristicCost + cost;
    boolean isOpen = openCells.contains(t);

    if (!isOpen || tFinalCost < t.finalCost) {
      t.finalCost = tFinalCost;
      t.parent = current;

      if (!isOpen) {
        openCells.add(t);
      }
    }
  }

  public void process() {
    //we add the start location to open list
    openCells.add(grid[startRow][startCol]);
    Node current;
    while (true) {
      current = openCells.poll();
      if (current == null) {
        break;
      }
      closedCells[current.row][current.col] = true;
      if (current.equals(grid[endRow][endCol])) {
        return;
      }

      Node t;

      if (current.row - 1 >= 0) {
        t = grid[current.row - 1][current.col];
        updateCostIfNeeded(current, t, current.finalCost + VHCOST);
      }

      if (current.col - 1 >= 0) {
        t = grid[current.row ][current.col-1];
        updateCostIfNeeded(current, t, current.finalCost + VHCOST);
      }

      if (current.col + 1  < grid[0].length) {
        t = grid[current.row ][current.col+1];
        updateCostIfNeeded(current, t, current.finalCost + VHCOST);
      }

      if (current.row + 1  < grid.length) {
        t = grid[current.row+1 ][current.col];
        updateCostIfNeeded(current, t, current.finalCost + VHCOST);
      }

    }
  }

  public void display(){
    System.out.println("Grid: ");
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        System.out.println("SO ");
      }
    }
  }
}*/
