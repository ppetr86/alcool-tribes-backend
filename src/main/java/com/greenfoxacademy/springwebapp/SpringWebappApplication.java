package com.greenfoxacademy.springwebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class SpringWebappApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringWebappApplication.class, args);
  }

}
