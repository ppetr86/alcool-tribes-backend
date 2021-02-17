package com.greenfoxacademy.springwebapp.resource.repositories;

import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<ResourceEntity, Long> {

  List<ResourceEntity> findAllByKingdomId(long id);
}
