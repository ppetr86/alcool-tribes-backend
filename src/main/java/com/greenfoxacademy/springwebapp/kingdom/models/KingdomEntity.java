package com.greenfoxacademy.springwebapp.kingdom.models;

import javax.persistence.*;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<BuildingEntity> buildings;

  @Column(name = "kingdomname")
  private String kingdomName;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "kingdom")
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<TroopEntity> troops;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "kingdom")
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<ResourceEntity> resources;

  @OneToOne
  private LocationEntity location;
}
