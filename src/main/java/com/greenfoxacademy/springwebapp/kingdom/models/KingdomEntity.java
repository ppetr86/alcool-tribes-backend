package com.greenfoxacademy.springwebapp.kingdom.models;

import com.greenfoxacademy.springwebapp.buildings.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
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
  private Set<BuildingEntity> building;

  @OneToMany
  @JoinColumn(name = "fk_resource_id")
  private Set<ResourceEntity> resources;

  @Column(name = "kingdomname")
  private String kingdomName;


  //TODO: need to add rest of fields
}