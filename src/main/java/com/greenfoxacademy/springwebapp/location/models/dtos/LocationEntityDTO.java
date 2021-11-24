package com.greenfoxacademy.springwebapp.location.models.dtos;

import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
