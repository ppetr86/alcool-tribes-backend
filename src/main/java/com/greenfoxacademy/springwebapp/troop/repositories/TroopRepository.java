package com.greenfoxacademy.springwebapp.troop.repositories;

import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TroopRepository extends JpaRepository<TroopEntity, Long> {

  TroopEntity save (TroopEntity troop);

  TroopEntity findTroopEntityByKingdomId(Long kingdomEntity);

  TroopEntity findTroopEntityById(Long troopId);

}