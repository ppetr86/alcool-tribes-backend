package com.greenfoxacademy.springwebapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Slf4j
@SpringBootApplication
public class SpringWebappApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringWebappApplication.class, args);
    log.warn("!!!!!!!!!!!!!!!Example log from {}");
  }

}
