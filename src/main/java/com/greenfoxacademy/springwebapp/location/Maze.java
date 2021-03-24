package com.greenfoxacademy.springwebapp.location;

import com.greenfoxacademy.springwebapp.location.models.Coordinate;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
public class Maze {
  private static final int ROAD = 0;
  private static final int WALL = 1;
  private static final int START = 2;
  private static final int EXIT = 3;
  private static final int SHORTEST_PATH = 4;

  private int[][] maze;
  private boolean[][] visited;
  private Coordinate start;
  private Coordinate end;

  public Maze(int[][] maze) {

    this.maze = maze;
    this.visited = new boolean[maze.length][maze[0].length];
  }

  public int getHeight() {
    return maze.length;
  }

  public int getWidth() {
    return maze[0].length;
  }

  public Coordinate getEntry() {
    return start;
  }

  public Coordinate getExit() {
    return end;
  }

  public boolean isExit(int x, int y) {
    return x == end.getX() && y == end.getY();
  }

  public boolean isStart(int x, int y) {
    return x == start.getX() && y == start.getY();
  }

  public boolean isExplored(int row, int col) {
    return visited[row][col];
  }

  public boolean isWall(int row, int col) {

    return maze[row][col] == WALL;
  }

  public void setVisited(int row, int col, boolean value) {
    visited[row][col] = value;
  }

  public boolean isValidLocation(int row, int col) {
    if (row < 0 || row >= getHeight() || col < 0 || col >= getWidth()) {
      return false;
    }
    return true;
  }

  public void reset() {
    for (int i = 0; i < visited.length; i++) {
      Arrays.fill(visited[i], false);
    }
  }
}