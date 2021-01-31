package com.greenfoxacademy.springwebapp.building.repositories;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<BuildingEntity, Long> {
  //TODO: ALTB-15
  @Query(value = "SELECT * FROM buildings WHERE fk_buildings_kingdom = :kingdomID", nativeQuery = true)
  List<BuildingEntity> findBuildingsByKingdomID(Long kingdomID);
}
