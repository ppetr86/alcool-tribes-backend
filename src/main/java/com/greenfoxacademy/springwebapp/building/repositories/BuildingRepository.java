package com.greenfoxacademy.springwebapp.building.repositories;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends JpaRepository<BuildingEntity, Long> {
  List<BuildingEntity> findAllByKingdomId(Long kingdomID);

}
