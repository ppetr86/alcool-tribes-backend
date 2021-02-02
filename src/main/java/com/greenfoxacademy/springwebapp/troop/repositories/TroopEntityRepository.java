package com.greenfoxacademy.springwebapp.troop.repositories;

import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

//TODO: ALTB-14
@Repository
public interface TroopEntityRepository extends JpaRepository<TroopEntity, Long> {

  @Query(value = "SELECT * FROM troops WHERE fk_kingdom_id = :id", nativeQuery = true)
  Set<TroopEntity> findAllByKingdomID(Long id);
}
