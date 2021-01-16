package com.greenfoxacademy.springwebapp.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class KingdomBuildingsController {

  private TimeService timeService;

  @PostMapping("/kingdom/buildings")
}
