package com.greenfoxacademy.springwebapp.repositories;

import com.greenfoxacademy.springwebapp.models.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends CrudRepository<UserEntity, Long> {
  UserEntity findByUsername(String username);
  UserEntity findByUsernameEqualsAndPasswordEquals(String username, String password);
}
