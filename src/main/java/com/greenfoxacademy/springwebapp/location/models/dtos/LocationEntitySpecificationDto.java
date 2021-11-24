package com.greenfoxacademy.springwebapp.location.models.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationEntitySpecificationDto {

    private long countOfLocationsHaveXBiggerThan;
    private long countOfLocationsHaveXBiggerThanAndTypeIs;
    private List<LocationEntityDTO> locationsHaveXBiggerThan = new ArrayList<>();
    private List<LocationEntityDTO> locationsHaveXBiggerThanAndTypeIs = new ArrayList<>();


}
