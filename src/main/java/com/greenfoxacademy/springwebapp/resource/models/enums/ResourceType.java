package com.greenfoxacademy.springwebapp.resource.models.enums;

public enum ResourceType {
  FOOD("food"),
  GOLD("gold");

  public final String resourceType;

  ResourceType(String resourceType){
    this.resourceType = resourceType;
  }
}
