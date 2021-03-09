package com.greenfoxacademy.springwebapp.kingdom.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.email.models.RegistrationTokenEntity;
import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomNameDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;
import com.greenfoxacademy.springwebapp.kingdom.repositories.KingdomRepository;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

  @Test
  public void changeKingdomNameShouldReturnUpdatedKingdom() {
    KingdomNameDTO nameDTO = new KingdomNameDTO("New Kingdom");
    List<BuildingEntity> fakeBuildings = new ArrayList<>();
    List<TroopEntity> fakeTroops = new ArrayList<>();
    List<ResourceEntity> fakeResources = new ArrayList<>();
    Set<RegistrationTokenEntity> tokens = new HashSet<>();
    PlayerEntity fakePlayer =
        new PlayerEntity(1L, "test", "test", "test@gmail.com", "avatar.test", 0, null, true, tokens);
    KingdomEntity kingdom = new KingdomEntity(1L, fakePlayer, fakeBuildings, "Old Kingdom", fakeTroops, fakeResources,
        new LocationEntity(1L, 10, 10,null, LocationType.KINGDOM ));

    KingdomResponseDTO result = kingdomService.changeKingdomName(kingdom, nameDTO);

    Assert.assertEquals("New Kingdom", result.getName());
  }

  @Test
  public void changeKingdomNameShouldNotReturnUpdatedKingdom() {
    KingdomNameDTO nameDTO = new KingdomNameDTO("Not New Kingdom");
    List<BuildingEntity> fakeBuildings = new ArrayList<>();
    List<TroopEntity> fakeTroops = new ArrayList<>();
    List<ResourceEntity> fakeResources = new ArrayList<>();
    Set<RegistrationTokenEntity> tokens = new HashSet<>();
    PlayerEntity fakePlayer =
        new PlayerEntity(1L, "test", "test", "test@gmail.com", "avatar.test", 0, null, true, tokens);
    KingdomEntity kingdom = new KingdomEntity(1L, fakePlayer, fakeBuildings, "Old Kingdom", fakeTroops, fakeResources,
        new LocationEntity(1L, 10, 10, null, LocationType.KINGDOM));

    KingdomResponseDTO result = kingdomService.changeKingdomName(kingdom, nameDTO);

    Assert.assertNotEquals("New Kingdom", result.getName());
  }
}