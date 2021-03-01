package com.greenfoxacademy.springwebapp.kingdom.repositories;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KingdomRepository extends JpaRepository<KingdomEntity, Long> {
  List<KingdomEntity> findKingdomEntitiesByLocationBetween(LocationEntity location, LocationEntity location2);
}
