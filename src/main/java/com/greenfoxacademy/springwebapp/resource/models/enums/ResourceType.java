package com.greenfoxacademy.springwebapp.resource.models.enums;

//TODO: ALTB-14
public enum ResourceType {
  FOOD("food"),
  GOLD("gold");

  public final String resourceType;

  ResourceType(String resourceType){
    this.resourceType = resourceType;
  }
}
