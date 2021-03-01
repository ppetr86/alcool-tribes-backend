package com.greenfoxacademy.springwebapp.resource.models;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.TimerTask;

@AllArgsConstructor
@Getter
public class ResourceTimerTask extends TimerTask {
  private ResourceEntity resource;
  private Integer generation;
  private BuildingEntity building;

  @Override
  public void run() {

  }
}
