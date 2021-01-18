package com.greenfoxacademy.springwebapp.repositories;

import com.greenfoxacademy.springwebapp.models.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleEntityRepository extends CrudRepository<RoleEntity, Long> {
  RoleEntity findByRole(String role);
}
