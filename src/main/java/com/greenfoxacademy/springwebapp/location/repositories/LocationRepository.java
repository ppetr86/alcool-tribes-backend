package com.greenfoxacademy.springwebapp.location.repositories;

import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

  @Procedure
  List<LocationEntity> generateNDesertsAndJungles(int n);

  List<LocationEntity> findAll();


  List<LocationEntity> findAllByTypeIs(LocationType type);

  @Query(value = "select max(id) from locations", nativeQuery = true)
  Long getMaxID();


  LocationEntity findByXAndY(int randX, int randY);
}
