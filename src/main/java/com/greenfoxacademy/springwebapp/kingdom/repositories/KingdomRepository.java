package com.greenfoxacademy.springwebapp.kingdom.repositories;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface KingdomRepository extends JpaRepository<KingdomEntity, Long> {

  @Query(value = "select kingdomname from kingdoms where id = :id", nativeQuery = true)
  String findKingdomNameByID(Long id);

}
