package com.greenfoxacademy.springwebapp.player.repositories;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

  @Query(value = "select is_account_verified from players where username = :username", nativeQuery = true)
  boolean isVerifiedUsername(String username);

  PlayerEntity findByUsername(String username);

  boolean existsByUsername(String username);
}