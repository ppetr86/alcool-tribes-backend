package com.greenfoxacademy.springwebapp.location.models;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationDistance {

  private long id;
  private int x;
  private int y;
  private KingdomEntity kingdom;
  private LocationType type;
  private int distance = Integer.MAX_VALUE;

  public LocationDistance(LocationEntity e) {
    this.id = e.getId();
    this.x = e.getX();
    this.y = e.getY();
    this.kingdom = e.getKingdom();
    this.type = e.getType();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocationDistance that = (LocationDistance) o;
    return x == that.x && y == that.y;
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
    return "LocationEntity "
        + " id=" + id
        + ", x=" + x
        + ", y=" + y
        + ", kingdom=" + kingdomName
        + ", type=" + type;
  }
}
