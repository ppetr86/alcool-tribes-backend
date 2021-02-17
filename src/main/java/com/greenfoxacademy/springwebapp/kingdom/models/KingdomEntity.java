package com.greenfoxacademy.springwebapp.kingdom.models;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kingdoms")
public class KingdomEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  private PlayerEntity player;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "kingdom")
  private List<BuildingEntity> buildings;

  @Column(name = "kingdomname")
  private String kingdomName;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "kingdom")
  private List<ResourceEntity> resources;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "kingdom")
  private List<TroopEntity> troops;

  public KingdomEntity(List<TroopEntity> troops) {
    this.troops = troops;
  }
}