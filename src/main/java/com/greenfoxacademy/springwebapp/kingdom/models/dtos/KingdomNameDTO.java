package com.greenfoxacademy.springwebapp.kingdom.models.dtos;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KingdomNameDTO {

    @NotEmpty(message = "Missing parameter(s): name!")
    private String name;
}
