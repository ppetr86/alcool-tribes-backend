package com.greenfoxacademy.springwebapp.location;

import com.greenfoxacademy.springwebapp.location.models.Coordinate;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class Maze {
  private static final int ROAD = 0;
  private static final int WALL = 1;
  private static final int START = 2;
  private static final int EXIT = 3;
  private static final int PATH = 4;

  private int[][] maze;
  private boolean[][] visited;
  private Coordinate start;
  private Coordinate end;

  public Maze(int[][] maze) {
    this.maze = maze;
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

  public void printPath(List<Coordinate> path) {
    int[][] tempMaze = Arrays.stream(maze)
        .map(int[]::clone)
        .toArray(int[][]::new);
    for (Coordinate coordinate : path) {
      if (isStart(coordinate.getX(), coordinate.getY()) || isExit(coordinate.getX(), coordinate.getY())) {
        continue;
      }
      tempMaze[coordinate.getX()][coordinate.getY()] = PATH;
    }
    System.out.println(toString(tempMaze));
  }

  public String toString(int[][] maze) {
    StringBuilder result = new StringBuilder(getWidth() * (getHeight() + 1));
    for (int row = 0; row < getHeight(); row++) {
      for (int col = 0; col < getWidth(); col++) {
        if (maze[row][col] == ROAD) {
          result.append(' ');
        } else if (maze[row][col] == WALL) {
          result.append('#');
        } else if (maze[row][col] == START) {
          result.append('S');
        } else if (maze[row][col] == EXIT) {
          result.append('E');
        } else {
          result.append('.');
        }
      }
      result.append('\n');
    }
    return result.toString();
  }

  public void reset() {
    for (int i = 0; i < visited.length; i++) {
      Arrays.fill(visited[i], false);
    }
  }
}
