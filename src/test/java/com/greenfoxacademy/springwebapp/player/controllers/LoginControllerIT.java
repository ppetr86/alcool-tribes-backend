package com.greenfoxacademy.springwebapp.player.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerDTO;
import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class LoginControllerIT {

  @Autowired
  private MockMvc mockMvc;

  private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
    MediaType.APPLICATION_JSON.getSubtype());

  @MockBean
  private JwtProvider jwtProviderMock;

  @Test
  public void postLoginShouldReturnCorrectPlayer() throws Exception {

    PlayerDTO request = new PlayerDTO("Mark", "mark");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

    mockMvc.perform(post("/login")
      .contentType(MediaType.APPLICATION_JSON)
      .content(json))
      .andExpect(status().isOk())
      .andExpect(content().contentType(contentType))
      .andExpect(jsonPath("$.username", is("Mark")));

  }


//
//    JwtProvider jwtProvider = Mockito.mock(JwtProvider.class);
//    Mockito.when(jwtProvider.generateToken(fakeUserDTO.getUsername())).thenReturn("12345");
//    //.andExpect(jsonPath("$.token", is("12345"))); Zdenek's solution
//
//
//    mockMvc.perform(
//      post(String.format("%s", "/login"))
//        .contentType(MediaType.APPLICATION_JSON)
//        .content(new ObjectMapper().writeValueAsString(fakeUserDTO)))
//      .andExpect(status().is(HttpStatus.OK.value()))
//      .andExpect(jsonPath("$.status", is("ok")))
//      .andExpect(jsonPath("$.token", is(jwtProviderMock.generateToken(fakeUserDTO.getUsername()))));
}

