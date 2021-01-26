package com.greenfoxacademy.springwebapp.player.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRequestDTO;
import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@EnableWebMvc
public class LoginControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private JwtProvider jwtProviderMock;

  @MockBean
  private PasswordEncoder passwordEncoder;  //Have to Mock the .matches method

  @Test
  public void postLoginShouldReturn200AndOkMessage() throws Exception {
    PlayerRequestDTO request = new PlayerRequestDTO("Mark", "markmark");

    String json = new ObjectMapper().writeValueAsString(request);

    Mockito.when(jwtProviderMock.generateToken(request.getUsername())).thenReturn("12345");
    Mockito.when(passwordEncoder.matches(request.getPassword(), "markmark")).thenReturn(true);

    mockMvc.perform(post("/login")
      .contentType(MediaType.APPLICATION_JSON)
      .content(json))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status", is("ok")))
      .andExpect(jsonPath("$.token", is("12345")));
  }

  @Test
  public void postLoginShouldReturn401ByBadUsername() throws Exception {
    PlayerRequestDTO request = new PlayerRequestDTO("BadUsername", "markmark");
    String json = new ObjectMapper().writeValueAsString(request);

    mockMvc.perform(post("/login")
      .contentType(MediaType.APPLICATION_JSON)
      .content(json))
      .andExpect(status().isUnauthorized())
      .andExpect(jsonPath("$.status", is("error")))
      .andExpect(jsonPath("$.message", is("Username or password is incorrect.")));
  }

  @Test
  public void postLoginShouldReturn401ByBadPassword() throws Exception {
    PlayerRequestDTO request = new PlayerRequestDTO("Mark", "badPassword");
    String json = new ObjectMapper().writeValueAsString(request);

    mockMvc.perform(post("/login")
      .contentType(MediaType.APPLICATION_JSON)
      .content(json))
      .andExpect(status().isUnauthorized())
      .andExpect(jsonPath("$.status", is("error")))
      .andExpect(jsonPath("$.message", is("Username or password is incorrect.")));
  }

  @Test
  public void postLoginShouldReturn400AndUsernameIsRequiredMessage() throws Exception {

    PlayerRequestDTO request = new PlayerRequestDTO(null, "markmark");
    String json = new ObjectMapper().writeValueAsString(request);

    mockMvc.perform(post("/login")
      .contentType(MediaType.APPLICATION_JSON)
      .content(json))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.status", is("error")))
      .andExpect(jsonPath("$.message", is("Username is required.")));
  }

  @Test
  public void postLoginShouldReturn400AndPasswordIsRequiredMessage() throws Exception {
    PlayerRequestDTO request = new PlayerRequestDTO("Mark", null);
    String json = new ObjectMapper().writeValueAsString(request);

    mockMvc.perform(post("/login")
      .contentType(MediaType.APPLICATION_JSON)
      .content(json))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.status", is("error")))
      .andExpect(jsonPath("$.message", is("Password is required.")));
  }

  @Test
  public void postLoginShouldReturn400AndUsernameAndPasswordAreRequiredMessage() throws Exception {
    PlayerRequestDTO request = new PlayerRequestDTO();
    String json = new ObjectMapper().writeValueAsString(request);

    mockMvc.perform(post("/login")
      .contentType(MediaType.APPLICATION_JSON)
      .content(json))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.status", is("error")))
      .andExpect(jsonPath("$.message", is("Username and password are required.")));
  }

  @Test
  public void postLoginShouldReturn400AndPasswordHasToContainAtLeast8LettersMessage() throws Exception {
    PlayerRequestDTO request = new PlayerRequestDTO("Mark", "mark");
    String json = new ObjectMapper().writeValueAsString(request);

    mockMvc.perform(post("/login")
      .contentType(MediaType.APPLICATION_JSON)
      .content(json))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.status", is("error")))
      .andExpect(jsonPath("$.message", is("Password has to contain at least 8 letters.")));
  }
}

