package com.greenfoxacademy.springwebapp.location.models.dtos;

import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import java.util.Optional;
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
        this.id = Optional.ofNullable(en).map(LocationEntity::getId).orElseGet(() -> 0L);
        this.x = Optional.ofNullable(en).map(LocationEntity::getX).orElseGet(() -> Integer.MAX_VALUE);
        this.y = Optional.ofNullable(en).map(LocationEntity::getY).orElseGet(() -> Integer.MAX_VALUE);
    }

    public static LocationEntityDTO copyUtil(LocationEntity en) {
        if (en == null) return null;
        LocationEntityDTO dto = new LocationEntityDTO();
        dto.id = Optional.ofNullable(en).map(LocationEntity::getId).orElseGet(() -> 0L);
        dto.x = Optional.ofNullable(en).map(LocationEntity::getX).orElseGet(() -> Integer.MAX_VALUE);
        dto.y = Optional.ofNullable(en).map(LocationEntity::getY).orElseGet(() -> Integer.MAX_VALUE);
        return dto;
    }
}
