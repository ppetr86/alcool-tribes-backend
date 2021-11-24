package com.greenfoxacademy.springwebapp.location.models.dtos;


import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationEntitySpecificationDto {

    private long countOfLocationsHaveXBiggerThan;
    private long countOfLocationsHaveXBiggerThanAndTypeIs;
    private List<LocationEntityDTO> locationsHaveXBiggerThan = new ArrayList<>();
    private List<LocationEntityDTO> locationsHaveXBiggerThanAndTypeIs = new ArrayList<>();


}
