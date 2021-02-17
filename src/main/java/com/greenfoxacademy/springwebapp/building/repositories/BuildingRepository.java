package com.greenfoxacademy.springwebapp.building.repositories;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<BuildingEntity, Long> {
  List<BuildingEntity> findAllByKingdomId(Long kingdomID);

}
