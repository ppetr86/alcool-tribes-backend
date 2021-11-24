package com.greenfoxacademy.springwebapp.player.repositories;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

    boolean existsByIdAndIsAccountVerified(long id, boolean isAccountVerified);


    boolean existsByUsername(String username);


    @Query(value = "SELECT id FROM players WHERE is_account_verified = :isAccountVerified", nativeQuery = true)
    List<Long> findAllByIsAccountVerified(boolean isAccountVerified);


    PlayerEntity findByUsername(String username);


    @Query(value = "select is_account_verified from players where username = :username", nativeQuery = true)
    boolean isVerifiedUsername(String username);


    @Query("SELECT p.isAccountVerified from PlayerEntity p WHERE p.id =:id")
    boolean selectIsVerified(long id);


    @Modifying
    //@Query(value = "UPDATE players SET is_account_verified = :isAccountVerified WHERE id = :id", nativeQuery = true)
    @Query("update PlayerEntity u set u.isAccountVerified = :isAccountVerified where u.id = :id")
    void setAccountVerified(long id, boolean isAccountVerified);


}