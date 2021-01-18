package com.greenfoxacademy.springwebapp.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "username", unique = true, length = 20)
  private String username;
  @Column(name = "password")
  private String password;
  @Column(name = "email")
  private String email;
  @Column(name = "kingdomname")
  private String kingdomName;

  public UserEntity(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.kingdomName = username + "'s kingdom";
  }

  public UserEntity(String username, String password, String email, String kingdomName) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.kingdomName = kingdomName;
  }
}
