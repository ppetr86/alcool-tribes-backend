package com.greenfoxacademy.springwebapp.player.repositories;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepo extends JpaRepository<PlayerEntity, Long> {

  PlayerEntity findByUsername(String username);

}
