package com.greenfoxacademy.springwebapp.kingdom.models;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import lombok.AllArgsConstructor;
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

  private boolean isSubscribedToChangesChat;

  @Override
  public String toString() {

    return new StringBuilder().append("KingdomEntity id")
        .append(this.getId() == null ? "no id" : String.valueOf(this.getId()))
        .append("kingdom name ").append(this.getKingdomName() == null ? "no name" : this.getKingdomName())
        .append("player name ").append(this.getPlayer() == null ? "no playername" : this.getPlayer().getUsername())
        .append("buildings count ")
        .append(this.getBuildings() == null ? "no buildings" : String.valueOf(buildings.size()))
        .append("troops count ").append(this.getTroops() == null ? "no troops" : String.valueOf(troops.size()))
        .append("resources count ").append(this.getResources() == null ? " no resources" :
            String.valueOf(resources.stream().mapToInt(ResourceEntity::getAmount).sum()))
        .append(" location type ").append(this.getLocation() == null ? "no location " : this.getLocation().getType())
        .toString();
  }
}
