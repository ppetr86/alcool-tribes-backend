package com.greenfoxacademy.springwebapp.player.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.greenfoxacademy.springwebapp.buildings.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

  @Column(name = "buildings")
  @OneToMany(cascade = CascadeType.ALL)
  private List<BuildingEntity> listOfBuildings;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "kingdomId", referencedColumnName = "Id")
  private KingdomEntity kingdomEntity;

  public PlayerEntity(String username, String password, String email,
                      KingdomEntity kingdomEntity) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.kingdomEntity = kingdomEntity;
  }

  public PlayerEntity(String username, String password, String email,
                      List<BuildingEntity> listOfBuildings,
                      KingdomEntity kingdomEntity) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.listOfBuildings = listOfBuildings;
    this.kingdomEntity = kingdomEntity;
  }
}
