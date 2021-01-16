package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.models.BuildingEntity;

public interface BuildingService {

  void save(BuildingEntity entity);

  void defineFinishedAt(BuildingEntity entity);
}
