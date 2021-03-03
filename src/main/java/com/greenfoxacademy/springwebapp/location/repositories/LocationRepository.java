package com.greenfoxacademy.springwebapp.location.repositories;

import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

  @Procedure
  List<LocationEntity> generate50DesertsAnd50Jungles();
}
