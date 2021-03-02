package com.greenfoxacademy.springwebapp.player.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegisterRequestDTO;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestNoSecurityConfig.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PlayerControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void postRegisterRequestShouldReturn201() throws Exception {

    PlayerRegisterRequestDTO rqst =
        new PlayerRegisterRequestDTO("testUser", "testPassword", "email@gmail.com");

    String requestJson = new ObjectMapper().writeValueAsString(rqst);

    mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(greaterThan(0))))
        .andExpect(jsonPath("$.username", is("testUser")))
        .andExpect(jsonPath("$.email", is("email@gmail.com")))
        .andExpect(jsonPath("$.kingdomId", is(greaterThan(0))))
        .andExpect(jsonPath("$.avatar", is("http://avatar.loc/my.png")))
        .andExpect(jsonPath("$.points", is(0)));
  }

  @Test
  public void postRegisterRequestShouldReturn400_OnMissingUsernmae() throws Exception {

    PlayerRegisterRequestDTO rqst = new PlayerRegisterRequestDTO();
    rqst.setEmail("email@email.com");
    rqst.setPassword("testPassword");

    String requestJson = new ObjectMapper().writeValueAsString(rqst);

    mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Username is required.")));
  }

  @Test
  public void postRegisterRequestShouldReturn400_OnMissingPwd() throws Exception {
    PlayerRegisterRequestDTO rqst = new PlayerRegisterRequestDTO();
    rqst.setEmail("email@email.com");
    rqst.setUsername("helloUsername");
    String requestJson = new ObjectMapper().writeValueAsString(rqst);
    mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Password is required.")));
  }

  @Test
  public void postRegisterRequestShouldReturn400_OnMissingPwdAndUsername() throws Exception {
    PlayerRegisterRequestDTO rqst = new PlayerRegisterRequestDTO();
    rqst.setEmail("email@email.com");
    String requestJson = new ObjectMapper().writeValueAsString(rqst);
    mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Password and username are required.")));
  }

  @Test
  public void postRegisterRequestShouldReturn406AndPasswordSizeError() throws Exception {
    PlayerRegisterRequestDTO rqst = new PlayerRegisterRequestDTO();
    rqst.setUsername("usernameTest");
    rqst.setPassword("123");
    rqst.setEmail("email@email.com");
    String requestJson = new ObjectMapper().writeValueAsString(rqst);
    mockMvc.perform(post("/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().isNotAcceptable())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Password must be 8 characters.")));
  }

  @Test
  public void postRegisterRequestShouldReturn409_UsernameIsTakenException() throws Exception {
    PlayerRegisterRequestDTO rqst = new PlayerRegisterRequestDTO();
    rqst.setUsername("occupied_username");
    rqst.setPassword("12345678");
    rqst.setEmail("email@email.com");
    String requestJson = new ObjectMapper().writeValueAsString(rqst);
    mockMvc.perform(post("/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Username is already taken.")));
  }
}