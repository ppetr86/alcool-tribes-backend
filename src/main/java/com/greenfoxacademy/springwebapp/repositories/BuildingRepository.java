package com.greenfoxacademy.springwebapp.repositories;

import com.greenfoxacademy.springwebapp.models.BuildingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends JpaRepository<BuildingEntity,Long> {
}
