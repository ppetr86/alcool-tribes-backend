package com.greenfoxacademy.springwebapp.player.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.player.models.dtos.UserDTO;
import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.core.Is.is;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class LoginAPIControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private JwtProvider jwtProviderMock;

  @Test
  public void postLoginShouldReturnCorrectPlayer() throws Exception {

    UserDTO fakeUserDTO = new UserDTO("Mark", "mark");

    mockMvc.perform(
      post(String.format("%s", "/login"))
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(fakeUserDTO)))
      .andExpect(status().is(HttpStatus.OK.value()))
      .andExpect(jsonPath("$.status", is("ok")))
      .andExpect(jsonPath("$.token", is(jwtProviderMock.generateToken(fakeUserDTO.getUsername()))));
  }
}
