package com.greenfoxacademy.springwebapp.player.models.dtos;

import lombok.Data;

import java.util.List;

@Data
public class PlayerListResponseDTO {

  private List<PlayerResponseDTO> players;

  public PlayerListResponseDTO(List<PlayerResponseDTO> players) {
    this.players = players;
  }
}
