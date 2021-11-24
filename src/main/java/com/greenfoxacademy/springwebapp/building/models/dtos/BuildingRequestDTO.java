package com.greenfoxacademy.springwebapp.building.models.dtos;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BuildingRequestDTO {

    @NotEmpty(message = "Missing parameter(s): type!")
    private String type;
}
