package com.greenfoxacademy.springwebapp.location.repositories;

import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

  @Procedure
  List<LocationEntity> generateNDesertsAndJunglesRandSelect(int n);

  @Procedure
  List<LocationEntity> generateEmptyLocations(int n);

  List<LocationEntity> findAll();

  List<LocationEntity> findAllLocationsByTypeIs(LocationType type);

  @Query(value = "select max(id) from locations", nativeQuery = true)
  Long getMaxID();

  @Query(value = "select * from locations where x=:x AND y=:y", nativeQuery = true)
  LocationEntity findByXIsAndYIs(int x, int y);

  long countAllByTypeIs(LocationType type);
}
