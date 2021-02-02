package com.greenfoxacademy.springwebapp.kingdom.repositories;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
//TODO: ALTB-14
@Repository
public interface KingdomEntityRepository extends JpaRepository<KingdomEntity, Long> {
}
