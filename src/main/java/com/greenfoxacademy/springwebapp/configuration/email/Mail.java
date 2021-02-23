package com.greenfoxacademy.springwebapp.configuration.email;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class Mail {

    private String from;
    private String to;
    private String subject;
    private Map<String, Object> model;
}
