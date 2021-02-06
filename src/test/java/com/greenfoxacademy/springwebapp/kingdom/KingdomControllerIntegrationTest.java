package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.kingdom.controllers.KingdomController;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.greenfoxacademy.springwebapp.factories.AuthFactory.createAuth;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestNoSecurityConfig.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class KingdomControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private KingdomService kingdomService;

  private Authentication authentication;

  /*@Before
  public void setUp() throws Exception {
    authentication = createAuth("Furkesz", 1L);
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
  }*/

  @Test
  public void getKingdomID_returnsCorrectResultsWhenExistingKingdom() throws Exception {
    String pathVar = "/1";
    mockMvc.perform(get("/kingdom/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is("1")))
            .andExpect(jsonPath("$.name", is("furkesz's kingdom")))
            .andExpect(jsonPath("$.userId", is(1)))
            .andExpect(jsonPath("$.buildings.[0].type", is("townhall")))
            .andExpect(jsonPath("$.resources.[0].type", is("food")))
            .andExpect(jsonPath("$.troops.[0].level", is(1)))
            .andExpect(jsonPath("$.location.x", is(10)));
  }

  @Test
  public void getKingdomID_returns404WhenNotExistingKingdom() throws Exception{
    String pathVar = "/1111";
    mockMvc.perform(get("/kingdom/1111"))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Id not found")));
  }
}