package com.greenfoxacademy.springwebapp.kingdom.services;

import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;
import com.greenfoxacademy.springwebapp.kingdom.repositories.KingdomRepository;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class KingdomServiceTest {
  private KingdomService kingdomService;
  private KingdomRepository kingdomRepository;

  @Before
  public void init() {
    kingdomRepository = Mockito.mock(KingdomRepository.class);
    kingdomService = new KingdomServiceImpl(kingdomRepository);
  }

  @Test
  public void findByID_ReturnsCorrectKingdom() {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setKingdomName("TEST_KINGDOM");
    kingdom.setId(2L);
    Mockito.when(kingdomRepository.findById(2L)).thenReturn(java.util.Optional.of(kingdom));
    Assert.assertEquals(kingdom, kingdomService.findByID(2L));
    Assert.assertEquals("TEST_KINGDOM", kingdomService.findByID(2L).getKingdomName());
  }

  @Test
  public void entityToKingdomResponseDTO_returnsCorrectResults() {
    KingdomEntity kingdom = KingdomFactory.createFullKingdom(1L, 1L);
    Mockito.when(kingdomRepository.findById(1L)).thenReturn(java.util.Optional.of(kingdom));
    KingdomResponseDTO result = kingdomService.entityToKingdomResponseDTO(1L);

    Assert.assertEquals("testKingdom", result.getName());
    Assert.assertEquals(1, result.getUserId());
    Assert.assertEquals(4, result.getBuildings().size());
    Assert.assertEquals(2, result.getTroops().size());
    Assert.assertEquals(2, result.getResources().size());
    Assert.assertEquals("gold", result.getResources().get(0).getType());
    Assert.assertEquals("mine", result.getBuildings().get(3).getType());
  }

  @Test(expected = IdNotFoundException.class)
  public void entityToKingdomResponseDTO_throwsIDNotFoundException() {
    KingdomResponseDTO result = kingdomService.entityToKingdomResponseDTO(null);
  }
}