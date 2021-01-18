package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.models.dtos.UserDTO;
import com.greenfoxacademy.springwebapp.services.LoginExceptionService;
import com.greenfoxacademy.springwebapp.services.UserEntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginAPIController {

  private UserEntityService userService;
  private LoginExceptionService loginExceptionService;

  public LoginAPIController(UserEntityService userService, LoginExceptionService loginExceptionService) {
    this.userService = userService;
    this.loginExceptionService = loginExceptionService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> postLogin(@RequestBody UserDTO userDTO){
    return loginExceptionService.loginExceptions(userDTO);
  }
}
