package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.factories.BuildingFactory;
import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.factories.ResourceFactory;
import com.greenfoxacademy.springwebapp.factories.TroopFactory;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;
import com.greenfoxacademy.springwebapp.kingdom.repositories.KingdomRepository;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomServiceImpl;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.enums.ResourceType;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class KingdomServiceTest {
  private KingdomService kingdomService;
  private KingdomRepository kingdomRepository;

  @Before
  public void init() {
    kingdomRepository = Mockito.mock(KingdomRepository.class);
    kingdomService = new KingdomServiceImpl(kingdomRepository);
  }

  @Test
  public void findByID_ReturnsCorrectKingdom(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setKingdomName("TEST_KINGDOM");
    kingdom.setId(2L);
    Mockito.when(kingdomRepository.findById(2L)).thenReturn(java.util.Optional.of(kingdom));
    Assert.assertEquals(kingdom, kingdomService.findByID(2L));
    Assert.assertEquals("TEST_KINGDOM", kingdomService.findByID(2L).getKingdomName());
  }

  @Test
  public void entityToKingdomResponseDTO_returnsCorrectResults(){
    KingdomEntity kingdom = KingdomFactory.createFullKingdom(1L,1L);
    KingdomResponseDTO result = kingdomService.entityToKingdomResponseDTO(kingdom);

    Assert.assertEquals("testKingdom", result.getName());
    Assert.assertEquals(1, result.getUserId());
    Assert.assertEquals(2, result.getBuildings().size());
    Assert.assertEquals(2, result.getTroops().size());
    Assert.assertEquals(2, result.getResources().size());
    Assert.assertEquals("gold", result.getResources().get(0).getType());
    Assert.assertEquals("mine", result.getBuildings().get(1).getType());
  }

  @Test(expected = IdNotFoundException.class)
  public void entityToKingdomResponseDTO_throwsIDNotFoundException(){
    KingdomEntity nullKingdom = null;
    KingdomResponseDTO result = kingdomService.entityToKingdomResponseDTO(nullKingdom);
  }
}