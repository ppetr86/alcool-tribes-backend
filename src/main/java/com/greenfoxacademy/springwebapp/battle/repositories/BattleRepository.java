package com.greenfoxacademy.springwebapp.battle.repositories;

import com.greenfoxacademy.springwebapp.battle.models.BattleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BattleRepository extends JpaRepository<BattleEntity, Long> {

  //repository methods to be defined here

}
