package com.greenfoxacademy.springwebapp.resource.repositories;

import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceEntityRepository extends JpaRepository<ResourceEntity, Long> {
}
