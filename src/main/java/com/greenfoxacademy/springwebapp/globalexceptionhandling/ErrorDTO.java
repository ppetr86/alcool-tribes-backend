package com.greenfoxacademy.springwebapp.globalexceptionhandling;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ErrorDTO {

    private String status;
    private String message;

    public ErrorDTO(String message) {
        this.message = message;
        this.status = "error";
    }
}