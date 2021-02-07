package com.greenfoxacademy.springwebapp.location.models.dtos;

import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationEntityDTO {

  long id;
  int x;
  int y;

  public LocationEntityDTO(LocationEntity en) {
    this.id = en.getId();
    this.x = en.getX();
    this.y = en.getY();
  }
}
