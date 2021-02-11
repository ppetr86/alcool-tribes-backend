package com.greenfoxacademy.springwebapp.troop.repositories;

import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TroopRepository extends JpaRepository <TroopEntity, Long> {

  TroopEntity save (TroopEntity troop);
}
