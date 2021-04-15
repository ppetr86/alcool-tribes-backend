package com.greenfoxacademy.springwebapp.player.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeletedPlayerDTO {

  private boolean deleted;
  private String deletedPlayerName;

  public DeletedPlayerDTO(boolean deleted, String deletedPlayerName) {
    this.deleted = deleted;
    this.deletedPlayerName = deletedPlayerName + " player deleted.";
  }
}
