package com.greenfoxacademy.springwebapp.location.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Node {
  //coordinates
  int row;
  int col;
  // parent node
  public Node parent;
  //heuristic cost of current node
  public int heuristicCost;
  // cost of the path from start node to this node and some estimate of cheapest path
  public int finalCost;

  public boolean solution;

  public Node(int row, int col) {
    this.row = row;
    this.col = col;
  }

  @Override
  public String toString() {
    return "[" +row + ", " +col + "]";
  }
}
