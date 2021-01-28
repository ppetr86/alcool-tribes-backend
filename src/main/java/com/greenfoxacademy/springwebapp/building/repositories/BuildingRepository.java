package com.greenfoxacademy.springwebapp.building.repositories;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<BuildingEntity, Long> {

  @Query(value = "SELECT * FROM buildings WHERE fk_kingdom_id = :kingdomID", nativeQuery = true)
  List<BuildingEntity> findBuildingsByKingdomID(Long kingdomID);
}
