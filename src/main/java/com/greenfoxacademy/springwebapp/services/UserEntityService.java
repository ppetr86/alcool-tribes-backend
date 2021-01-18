package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.models.UserEntity;

public interface UserEntityService {
  long countUsers();
  UserEntity saveUser(UserEntity userEntity);
  UserEntity findByUsername(String username);
  UserEntity findByUsernameAndPassword(String username, String password);
}
