package com.greenfoxacademy.springwebapp.player.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegistrationRequestDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PlayerControllerIT {
  @Autowired
  private MockMvc mockMvc;

  @Test
  public void postRegisterRequestShouldReturn201() throws Exception {

    PlayerRegistrationRequestDTO playerRegistrationRequestDTO =
        new PlayerRegistrationRequestDTO("testUser", "testPassword", "email@email.com");

    String requestJson = new ObjectMapper().writeValueAsString(playerRegistrationRequestDTO);

    mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().isCreated())
        .andExpect(content().json("{id: 1," +
            "username: testUser," +
            "email: email@email.com" +
            "kingdomId: 1," +
            "avatar: http://avatar.loc/my.png" +
            "points: 0" +
            "}"));
  }

  @Test
  public void postRegisterRequestShouldReturn400() throws Exception {

    PlayerRegistrationRequestDTO playerRegistrationRequestDTO = new PlayerRegistrationRequestDTO();
    playerRegistrationRequestDTO.setEmail("email@email.com");
    playerRegistrationRequestDTO.setPassword("testPassword");

    String requestJson = new ObjectMapper().writeValueAsString(playerRegistrationRequestDTO);

    mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Username is required.")));
  }

  @Test
  public void postRegisterRequestShouldReturn406AndPasswordSizeError() throws Exception {

    PlayerRegistrationRequestDTO playerRegistrationRequestDTO = new PlayerRegistrationRequestDTO();
    playerRegistrationRequestDTO.setUsername("usernameTest");
    playerRegistrationRequestDTO.setPassword("123");
    playerRegistrationRequestDTO.setEmail("email@email.com");

    String requestJson = new ObjectMapper().writeValueAsString(playerRegistrationRequestDTO);

    mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().isNotAcceptable())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Password must be 8 characters.")));
  }

}
