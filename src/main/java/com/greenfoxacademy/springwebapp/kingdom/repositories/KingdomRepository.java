package com.greenfoxacademy.springwebapp.kingdom.repositories;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KingdomRepository extends JpaRepository<KingdomEntity, Long> {
  List<KingdomEntity> findKingdomEntitiesByLocationBetween(LocationEntity location, LocationEntity location2);

//  @Query(value = "SELECT u from KingdomEntity u where  u.locat BETWEEN  :location AND :location2")
//  List<KingdomEntity> findKingdomEntitiesByLocationBetween(LocationEntity location, LocationEntity location2);

//  @Query(value = "SELECT u from KingdomEntity u where  u.location.x = ?1 and u.location.y ")
//  List<KingdomEntity> findKingdomEntitiesByLocationIntArray(Integer statingX, Integer statingY, Integer endingX, Integer endingY);
}
