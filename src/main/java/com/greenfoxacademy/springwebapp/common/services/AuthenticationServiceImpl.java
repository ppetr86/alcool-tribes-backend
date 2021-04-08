package com.greenfoxacademy.springwebapp.common.services;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.enums.RoleType;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{

  private final PlayerService playerService;

  @Override
  public boolean hasAccess(Long id) {
    PlayerEntity player = playerService.findById(id);
    if (!player.getRoleType().equals(RoleType.ROLE_ADMIN)) throw new ForbiddenActionException();
    return true;
  }
}
