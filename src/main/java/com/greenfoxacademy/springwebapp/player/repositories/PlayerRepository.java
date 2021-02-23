package com.greenfoxacademy.springwebapp.player.repositories;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

  @Query(value = "select is_verified from players where username = :username", nativeQuery = true)
  boolean isVerifiedUsername(String username);
  PlayerEntity findByUsername(String username);

  @Modifying
  @Query(value = "update players set is_verified = :isVerified where id = :id", nativeQuery = true)
  void updateIsVefifiedOnPlayer(long id, boolean isVerified);

  @Query(value = "select id from players where username = :username", nativeQuery = true)
  Long findPlayerIDByUsername(String username);
}