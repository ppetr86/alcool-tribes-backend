package com.greenfoxacademy.springwebapp.registerTests.integration_test_register_endpoint;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.greenfoxacademy.springwebapp.register.models.dtos.PlayerDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationEndpointTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  public void postRegisterRequestShouldReturn201() throws Exception {

    PlayerDTO playerDTO = new PlayerDTO();
    playerDTO.setEmail("email@email.com");
    playerDTO.setUsername("testUser");
    playerDTO.setPassword("testPassword");

    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

    ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();

    String requestJson = writer.writeValueAsString(playerDTO);

    mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().isCreated());
  }

  @Test
  public void postRegisterRequestShouldReturn400() throws Exception {

    PlayerDTO playerDTO = new PlayerDTO();
    playerDTO.setEmail("email@email.com");
    playerDTO.setPassword("testPassword");

    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

    ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();

    String requestJson = writer.writeValueAsString(playerDTO);

    mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().isBadRequest());
  }

}
