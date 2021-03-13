package com.greenfoxacademy.springwebapp;

import com.greenfoxacademy.springwebapp.location.services.LocationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringWebappApplication {

  private LocationService locationService;

  public static void main(String[] args) {
    SpringApplication.run(SpringWebappApplication.class, args);
  }
}