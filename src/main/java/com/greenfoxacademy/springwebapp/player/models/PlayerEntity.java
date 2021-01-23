package com.greenfoxacademy.springwebapp.player.models;

import lombok.AllArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "player_table")
@AllArgsConstructor
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

  public PlayerEntity() {
  }

  public PlayerEntity(@NotNull String username) {
    this.username = username;
  }

  public PlayerEntity(@NotNull String username, @NotNull String password) {
    this.username = username;
    this.password = password;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
