package com.greenfoxacademy.springwebapp.building.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "buildings")
public class BuildingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false)
  private Long id;
  @Column(updatable = false)
  @Enumerated(EnumType.STRING)
  private BuildingType type;
  private Integer level;
  private Integer hp;
  @Column(updatable = false)
  private Long startedAt;
  @Column(updatable = false)
  private Long finishedAt;
  @JsonIgnore
  @ManyToOne
  private KingdomEntity kingdom;

  public BuildingEntity(KingdomEntity kingdom, BuildingType type, int level, int hp, Long startedAt, Long finishedAt) {
    this.type = type;
    this.level = level;
    this.kingdom = kingdom;
    this.hp = hp;
    this.startedAt = startedAt;
    this.finishedAt = finishedAt;
  }

  //constructor for tests building factory
  public BuildingEntity(Long id,
                        BuildingType type, Integer level, Integer hp, Long startedAt,
                        Long finishedAt) {
    this.id = id;
    this.type = type;
    this.level = level;
    this.hp = hp;
    this.startedAt = startedAt;
    this.finishedAt = finishedAt;
  }
}

