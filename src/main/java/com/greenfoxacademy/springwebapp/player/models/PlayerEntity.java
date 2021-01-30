package com.greenfoxacademy.springwebapp.player.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "player_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @NotNull
  private String username;

  @Column
  @NotNull
  private String password;

  public PlayerEntity(@NotNull String username, @NotNull String password) {
    this.username = username;
    this.password = password;
  }
}
