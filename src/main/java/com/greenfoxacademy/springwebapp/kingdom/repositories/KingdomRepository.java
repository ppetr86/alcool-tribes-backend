package com.greenfoxacademy.springwebapp.kingdom.repositories;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface KingdomRepository extends JpaRepository<KingdomEntity, Long>, JpaSpecificationExecutor<KingdomEntity> {

    KingdomEntity findKingdomEntityByPlayer(PlayerEntity playerEntity);


    @Query(value = "select kingdomname from kingdoms where player_id = :id", nativeQuery = true)
    String findKingdomNameByPlayerID(Long id);

}
