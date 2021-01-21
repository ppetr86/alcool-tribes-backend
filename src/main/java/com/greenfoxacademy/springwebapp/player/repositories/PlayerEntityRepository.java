package com.greenfoxacademy.springwebapp.player.repositories;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerEntityRepository extends CrudRepository<PlayerEntity, Long> {
  List<PlayerEntity> findAll();
  PlayerEntity findByUsername(String username);
  PlayerEntity findByUsernameEqualsAndPasswordEquals(String username, String password);
}
