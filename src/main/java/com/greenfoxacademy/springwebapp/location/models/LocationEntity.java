package com.greenfoxacademy.springwebapp.location.models;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "locations")
public class LocationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Integer x;
  private Integer y;

  @OneToOne
  private KingdomEntity kingdom;

  @Enumerated(EnumType.STRING)
  private LocationType type;

  public LocationEntity(Integer x, Integer y, KingdomEntity kingdom,
                        LocationType type) {
    this.x = x;
    this.y = y;
    this.kingdom = kingdom;
    this.type = type;
  }

  public LocationEntity(LocationType kingdom) {
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocationEntity that = (LocationEntity) o;
    return x.equals(that.x) && y.equals(that.y);
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    String kingdomName = "";
    if (kingdom == null) {
      kingdomName = " this is not a kingdom";
    } else {
      kingdomName = kingdom.getKingdomName();
    }
    return "LocationEntity" +
        "id=" + id +
        ", x=" + x +
        ", y=" + y +
        ", kingdom=" + kingdomName  +
        ", type=" + type;
  }
}
