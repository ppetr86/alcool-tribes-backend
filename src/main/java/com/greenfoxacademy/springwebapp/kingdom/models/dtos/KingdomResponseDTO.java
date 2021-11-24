package com.greenfoxacademy.springwebapp.kingdom.models.dtos;

import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingSingleResponseDTO;
import com.greenfoxacademy.springwebapp.location.models.dtos.LocationEntityDTO;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceResponseDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class KingdomResponseDTO {

    private long id;
    private String name;
    private long userId;
    private List<BuildingSingleResponseDTO> buildings;
    private List<ResourceResponseDTO> resources;
    private List<TroopEntityResponseDTO> troops;
    private LocationEntityDTO location;

}