package com.greenfoxacademy.springwebapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
  private String avatar = "http://avatar.loc/my.png";
  @Column(name = "points")
  private int points = 0;

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
}
