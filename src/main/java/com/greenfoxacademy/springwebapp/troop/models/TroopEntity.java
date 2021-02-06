package com.greenfoxacademy.springwebapp.troop.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "troops")
@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class TroopEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private int level;
  private int hp;
  private int attack;
  private int defence;
  private long startedAt;
  private long finishedAt;

  public TroopEntity(Long id) {
    this.id = id;
  }
}