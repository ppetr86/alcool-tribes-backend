package com.greenfoxacademy.springwebapp.kingdom.models;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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

@Entity
@Getter
@Setter
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

  @OneToOne(mappedBy = "kingdom", cascade = CascadeType.PERSIST)
  private LocationEntity location;

  @Override
  public String toString() {
    return "KingdomEntity{" +
        "id=" + id +
        ", player name=" + player.getUsername()+
        ", buildings count=" + (long) buildings.size() +
        ", kingdomName='" + kingdomName + '\'' +
        ", troops count=" + (long) troops.size() +
        ", resources sum=" + resources.stream().mapToInt(ResourceEntity::getAmount).sum()+
        ", location type=" + location.getType() +
        '}';
  }
}
