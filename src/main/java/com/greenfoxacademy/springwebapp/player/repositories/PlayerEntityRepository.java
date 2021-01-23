package com.greenfoxacademy.springwebapp.player.repositories;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerEntityRepository extends CrudRepository<PlayerEntity, Long> {
  PlayerEntity findByUsername(String username);
}
