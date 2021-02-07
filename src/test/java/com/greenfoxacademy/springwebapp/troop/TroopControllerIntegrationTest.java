package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import com.greenfoxacademy.springwebapp.troop.controllers.TroopController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.greenfoxacademy.springwebapp.factories.AuthFactory.createAuth;


@Import(TestNoSecurityConfig.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TroopControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  private Authentication authentication;

  @Before
  public void setUp() throws Exception {
    authentication = createAuth("Furkesz", 1L);
  }

  @Test
  public void getTroopsOfKingdom_ReturnsCorrectBodyAndStatus() throws Exception {
    mockMvc.perform(get(TroopController.URI_KINGDOM_TROOPS)
            .principal(authentication))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.troops[0].level", is(1)))
            .andExpect(jsonPath("$.troops[0].hp", is(100)));
  }
}