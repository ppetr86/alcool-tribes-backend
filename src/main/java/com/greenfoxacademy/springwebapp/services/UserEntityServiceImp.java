package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.models.RoleEntity;
import com.greenfoxacademy.springwebapp.models.UserEntity;
import com.greenfoxacademy.springwebapp.repositories.RoleEntityRepository;
import com.greenfoxacademy.springwebapp.repositories.UserEntityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserEntityServiceImp implements UserEntityService{

  private UserEntityRepository userEntityRepository;
  private RoleEntityRepository roleEntityRepository;
  private PasswordEncoder passwordEncoder;

  public UserEntityServiceImp(UserEntityRepository userEntityRepository, RoleEntityRepository roleEntityRepository, PasswordEncoder passwordEncoder) {
    this.userEntityRepository = userEntityRepository;
    this.roleEntityRepository = roleEntityRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public long countUsers() {
    return userEntityRepository.count();
  }

  @Override
  public UserEntity saveUser(UserEntity userEntity) {
    RoleEntity userRole = roleEntityRepository.findByRole("ROLE_USER");
    userEntity.setRoleEntity(userRole);
    userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
    return userEntityRepository.save(userEntity);
  }

  @Override
  public UserEntity findByUsername(String username) {
    return userEntityRepository.findByUsername(username);
  }

  @Override
  public UserEntity findByUsernameAndPassword(String username, String password) {
    UserEntity userEntity = findByUsername(username);

    if (userEntity != null){
      if (passwordEncoder.matches(password, userEntity.getPassword())){
        return userEntity;
      }
    }
    return null;
  }
}
