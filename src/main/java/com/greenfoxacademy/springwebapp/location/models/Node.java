package com.greenfoxacademy.springwebapp.location.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Node {
  public int[] coordinate = new int[2];
  public int data;
  public Node Right, Left, Top, Bottom;

  @Override
  public String toString() {
    return Arrays.toString(coordinate) + "|";
  }
}
