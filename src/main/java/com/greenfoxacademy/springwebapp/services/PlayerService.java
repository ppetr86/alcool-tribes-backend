package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.models.Player;

public interface PlayerService {
  Player loadPlayerByUsername(String username);
}
