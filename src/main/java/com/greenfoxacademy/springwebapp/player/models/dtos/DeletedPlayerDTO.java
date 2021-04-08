package com.greenfoxacademy.springwebapp.player.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeletedPlayerDTO {

  private boolean deleted;
  private String deletedPlayer;


}
