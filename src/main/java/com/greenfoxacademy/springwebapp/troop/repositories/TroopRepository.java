package com.greenfoxacademy.springwebapp.troop.repositories;

import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TroopRepository extends JpaRepository<TroopEntity, Long> {

  TroopEntity save(TroopEntity troop);

  @Query("SELECT c.kingdom.id FROM TroopEntity c WHERE c.id = ?1")
  Long findKingdomIdByTroopId(Long troopId);
}