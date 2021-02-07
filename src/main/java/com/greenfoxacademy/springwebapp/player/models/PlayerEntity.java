package com.greenfoxacademy.springwebapp.player.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRequestDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "players")
public class PlayerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "username", unique = true, length = 20)
  private String username;
  @Column(name = "password")
  @JsonIgnore
  private String password;
  @Column(name = "email")
  private String email;
  @Column(name = "avatar")
  private String avatar = "http://avatar.loc/my.png"; //TODO: need to have proper avatar for every player
  @Column(name = "points")
  private int points = 0; //TODO: need to have proper point logic
  @OneToOne(mappedBy = "player", cascade = CascadeType.ALL)
  private KingdomEntity kingdom;

  public PlayerEntity(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public PlayerEntity(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }
}
