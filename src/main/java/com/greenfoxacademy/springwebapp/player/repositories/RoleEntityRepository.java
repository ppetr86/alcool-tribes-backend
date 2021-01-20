package com.greenfoxacademy.springwebapp.player.repositories;

import com.greenfoxacademy.springwebapp.player.models.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleEntityRepository extends CrudRepository<RoleEntity, Long> {
  RoleEntity findByRole(String role);
}
