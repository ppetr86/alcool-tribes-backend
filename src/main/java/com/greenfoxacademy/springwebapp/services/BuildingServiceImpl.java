package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.repositories.BuildingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BuildingServiceImpl implements BuildingService {

  private BuildingRepository repo;

  @Override
  public void save(BuildingEntity entity) {
    repo.save(entity);
  }

  @Override
  public void defineFinishedAt(BuildingEntity entity) {
    if (entity.getType().label.equals("townhall"))
      entity.setFinishedAt(entity.getStartedAt() + 120);

    if (entity.getType().label.equals("townhall") ||
            entity.getType().label.equals("farm") ||
            entity.getType().label.equals("mine"))
      entity.setFinishedAt(entity.getStartedAt() + 120);

    if ( entity.getType().label.equals("academy"))
      entity.setFinishedAt(entity.getStartedAt() + 120);
  }
}
