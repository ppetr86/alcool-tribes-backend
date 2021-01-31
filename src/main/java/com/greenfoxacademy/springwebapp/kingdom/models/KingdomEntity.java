package com.greenfoxacademy.springwebapp.kingdom.models;

import com.greenfoxacademy.springwebapp.buildings.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Table
@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class KingdomEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany()
  @JoinColumn(name = "fk_kingdom_id")
  private Set<BuildingEntity> buildings;

  @OneToMany
  @JoinColumn(name = "fk_kingdom_id")
  private Set<ResourceEntity> resources;

  @OneToMany
  @JoinColumn(name = "fk_kingdom_id")
  private Set<TroopEntity> troops;

  @Column(name = "kingdomname")
  private String kingdomName;

  @OneToOne(cascade = CascadeType.ALL)
  @PrimaryKeyJoinColumn
  private LocationEntity location;

  @OneToOne(cascade = CascadeType.ALL)
  @PrimaryKeyJoinColumn
  private PlayerEntity userID;


  //TODO: need to add rest of fields
}