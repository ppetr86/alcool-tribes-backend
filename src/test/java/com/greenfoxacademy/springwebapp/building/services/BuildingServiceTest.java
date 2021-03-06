package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.TestConfig;
import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingLevelDTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.factories.BuildingFactory;
import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.factories.PlayerFactory;
import com.greenfoxacademy.springwebapp.factories.ResourceFactory;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.TownhallLevelException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.enums.RoleType;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class BuildingServiceTest {

  private BuildingService buildingService;
  private BuildingRepository buildingRepository;
  private TimeService timeService;
  private ResourceService resourceService;

  @Before
  public void init() {
    buildingRepository = Mockito.mock(BuildingRepository.class);
    timeService = Mockito.mock(TimeService.class);
    resourceService = Mockito.mock(ResourceService.class);
    timeService = Mockito.mock(TimeService.class);
    resourceService = Mockito.mock(ResourceService.class);

    Environment mockEnvironment = TestConfig.mockEnvironment();
    buildingService = new BuildingServiceImpl(mockEnvironment, buildingRepository, timeService, resourceService);

    Mockito.when(buildingRepository.findAllByKingdomId(1L)).thenReturn(BuildingFactory.createDefaultBuildings(null));
  }

  @Test
  public void findBuildingsByKingdomId_correct() {
    Assert.assertEquals(4, buildingService.findBuildingsByKingdomId(1L).size());
  }

  @Test
  public void findBuildingsByKingdomId_wrong() {
    Assert.assertNotEquals(999, buildingService.findBuildingsByKingdomId(1L).size());
  }

  @Test
  public void defineFinishedAt_Townhall() {
    BuildingEntity b = BuildingFactory.createDefaultBuildings(null).get(0);
    buildingService.defineFinishedAt(b);
    Assert.assertEquals(120, b.getFinishedAt() - b.getStartedAt());
  }

  @Test
  public void defineFinishedAt_Farm() {
    BuildingEntity b = BuildingFactory.createDefaultBuildings(null).get(2);
    buildingService.defineFinishedAt(b);
    Assert.assertEquals(60, b.getFinishedAt() - b.getStartedAt());
  }

  @Test
  public void defineFinishedAt_Mine() {
    BuildingEntity b = BuildingFactory.createDefaultBuildings(null).get(3);
    buildingService.defineFinishedAt(b);
    Assert.assertEquals(60, b.getFinishedAt() - b.getStartedAt());
  }

  @Test
  public void defineFinishedAt_Academy() {
    BuildingEntity b = BuildingFactory.createDefaultBuildings(null).get(1);
    buildingService.defineFinishedAt(b);
    Assert.assertEquals(90, b.getFinishedAt() - b.getStartedAt());
  }

  @Test
  public void defineHP_Townhall() {
    BuildingEntity b = BuildingFactory.createDefaultBuildings(null).get(0);
    buildingService.defineHp(b);
    Assert.assertEquals(200, b.getHp().longValue());
  }

  @Test
  public void defineHP_Farm() {
    BuildingEntity b = BuildingFactory.createDefaultBuildings(null).get(2);
    buildingService.defineHp(b);
    Assert.assertEquals(100, b.getHp().longValue());
  }

  @Test
  public void defineHP_Mine() {
    BuildingEntity b = BuildingFactory.createDefaultBuildings(null).get(3);
    buildingService.defineHp(b);
    Assert.assertEquals(100, b.getHp().longValue());
  }

  @Test
  public void defineHP_Academy() {
    BuildingEntity b = BuildingFactory.createDefaultBuildings(null).get(1);
    buildingService.defineHp(b);
    Assert.assertEquals(150, b.getHp().longValue());
  }

  @Test
  public void isTypeOkRequest_Townhall_ShouldTrue_VariousCase() {
    Assert.assertEquals(BuildingType.TOWNHALL, buildingService.setBuildingTypeOnEntity("ToWNhall").getType());
  }

  @Test
  public void isTypeOkRequest_Townhall_ShouldTrue_LowerCase() {
    Assert.assertEquals(BuildingType.TOWNHALL, buildingService.setBuildingTypeOnEntity("townhall").getType());
  }

  @Test
  public void isTypeOkRequest_Townhall_ShouldTrue_UpperCase() {
    Assert.assertEquals(BuildingType.TOWNHALL, buildingService.setBuildingTypeOnEntity("TOWNHALL").getType());
  }

  @Test(expected = NullPointerException.class)
  public void isTypeOkRequest_Townhall_ShouldNull_UpperCase() {
    Assert.assertNull(buildingService.setBuildingTypeOnEntity("TOWNHALLL").getType());
  }

  @Test
  public void isTypeOkRequest_Farm_ShouldTrue_UpperCase() {
    Assert.assertEquals(BuildingType.FARM, buildingService.setBuildingTypeOnEntity("FARM").getType());
  }

  @Test
  public void isTypeOkRequest_Mine_ShouldTrue_LowerCase() {
    Assert.assertEquals(BuildingType.MINE,
        buildingService.setBuildingTypeOnEntity("mine").getType());
  }

  @Test
  public void isTypeOkRequest_Academy_ShouldTrue_VariousCase() {
    Assert.assertEquals(BuildingType.ACADEMY,
        buildingService.setBuildingTypeOnEntity("ACAdemy").getType());
  }

  @Test(expected = IdNotFoundException.class)
  public void updateBuildingShouldReturnIdNotFoundException() {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereTownHallsLevelFive());

    buildingService = Mockito.spy(BuildingService.class);
    Mockito.doReturn(kingdom.getBuildings()).when(buildingService).findBuildingsByKingdomId(kingdom.getId());
    Mockito.when(buildingService.updateBuilding(kingdom, 5L, new BuildingLevelDTO(2)))
        .thenThrow(IdNotFoundException.class);

    BuildingEntity result = buildingService.updateBuilding(kingdom, 5L, new BuildingLevelDTO(2));
  }

  @Test(expected = MissingParameterException.class)
  public void updateBuildingShouldReturnMissingParameterException() {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereTownHallsLevelFive());

    buildingService = Mockito.spy(BuildingService.class);
    Mockito.doReturn(kingdom.getBuildings()).when(buildingService).findBuildingsByKingdomId(kingdom.getId());
    Mockito.when(buildingService.updateBuilding(kingdom, 5L, new BuildingLevelDTO()))
        .thenThrow(MissingParameterException.class);

    BuildingEntity result = buildingService.updateBuilding(kingdom, 5L, new BuildingLevelDTO());
  }

  @Test(expected = ForbiddenActionException.class)
  public void updateBuildingShouldReturnForbiddenActionException() {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereTownHallsLevelFive());
    KingdomEntity kingdom2 = new KingdomEntity();
    kingdom2.setBuildings(BuildingFactory.createBuildingsWhereBuildingsIdAre_5_8(kingdom));

    buildingService = Mockito.spy(BuildingService.class);
    Mockito.doReturn(kingdom.getBuildings()).when(buildingService).findBuildingsByKingdomId(kingdom.getId());
    Mockito.when(buildingService.updateBuilding(kingdom, 5L, new BuildingLevelDTO(2)))
        .thenThrow(ForbiddenActionException.class);

    BuildingEntity result = buildingService.updateBuilding(kingdom, 5L, new BuildingLevelDTO(2));
  }

  @Test(expected = NotEnoughResourceException.class)
  public void updateBuildingShouldReturnNotEnoughResourceException() {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereTownHallsLevelFive());

    buildingService = Mockito.spy(BuildingService.class);
    Mockito.doReturn(kingdom.getBuildings()).when(buildingService).findBuildingsByKingdomId(kingdom.getId());
    Mockito.when(resourceService.hasResourcesForBuilding(kingdom, 200)).thenReturn(false);
    Mockito.when(buildingService.updateBuilding(kingdom, 1L, new BuildingLevelDTO(2)))
        .thenThrow(NotEnoughResourceException.class);

    BuildingEntity result = buildingService.updateBuilding(kingdom, 1L, new BuildingLevelDTO(2));
  }

  @Test(expected = TownhallLevelException.class)
  public void updateBuildingShouldReturnTownhallLevelException() {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereBuildingsIdAre_5_8(kingdom));

    buildingService = Mockito.spy(BuildingService.class);
    Mockito.doReturn(kingdom.getBuildings()).when(buildingService).findBuildingsByKingdomId(kingdom.getId());
    Mockito.when(resourceService.hasResourcesForBuilding(kingdom, 200)).thenReturn(true);
    Mockito.when(buildingService.updateBuilding(kingdom, 2L, new BuildingLevelDTO(2)))
        .thenThrow(TownhallLevelException.class);

    BuildingEntity result = buildingService.updateBuilding(kingdom, 2L, new BuildingLevelDTO(2));
  }

  @Test
  public void updateBuildingShouldReturnWithUpdatedTownHall() {
    KingdomEntity kingdom = KingdomFactory.createKingdomEntityWithId(1L);
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereBuildingsIdAre_5_8(kingdom));
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithHighAmount());
    kingdom.setPlayer(PlayerFactory.createPlayer(1L, kingdom));
    BuildingEntity townHall = kingdom.getBuildings().get(0);
    buildingService = Mockito.spy(buildingService);
    Mockito.doReturn(kingdom.getBuildings()).when(buildingService).findBuildingsByKingdomId(kingdom.getId());
    Mockito.when(buildingRepository.findById(5L)).thenReturn(Optional.of(townHall));
    Mockito.when(timeService.getTime()).thenReturn(1060L);
    Mockito.when(buildingRepository.save(any())).thenReturn(townHall);
    Mockito.when(resourceService.hasResourcesForBuilding(kingdom, 800)).thenReturn(true);

    BuildingEntity result = buildingService.updateBuilding(kingdom, 5L, new BuildingLevelDTO(4));

    Assert.assertEquals(Optional.of(4), Optional.ofNullable(result.getLevel()));
    Assert.assertEquals(Optional.of(800), Optional.ofNullable(result.getHp()));
    Assert.assertEquals(Optional.of(1060L), Optional.ofNullable(result.getStartedAt()));
    Assert.assertEquals(Optional.of(1540L), Optional.ofNullable(result.getFinishedAt()));
  }

  @Test
  public void updateBuildingShouldReturnWithUpdatedAcademy() {
    KingdomEntity kingdom = KingdomFactory.createKingdomEntityWithId(1L);
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereTownHallsLevelFive());
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithHighAmount());
    kingdom.setPlayer(PlayerFactory.createPlayer(1L, kingdom));
    BuildingEntity academy = kingdom.getBuildings().get(1);
    BuildingLevelDTO levelDTO = new BuildingLevelDTO(3);
    buildingService = Mockito.spy(buildingService);
    Mockito.doReturn(academy).when(buildingService).checkBuildingDetails(kingdom, academy.getId(), levelDTO);
    Mockito.when(buildingRepository.findById(academy.getId())).thenReturn(Optional.of(academy));
    Mockito.when(timeService.getTime()).thenReturn(1060L);
    Mockito.when(buildingRepository.save(any())).thenReturn(academy);
    Mockito.when(resourceService.hasResourcesForBuilding(kingdom, 300)).thenReturn(true);

    BuildingEntity result = buildingService.updateBuilding(kingdom, 2L, levelDTO);
    Assert.assertEquals(java.util.Optional.of(3), java.util.Optional.ofNullable(result.getLevel()));
    Assert.assertEquals(java.util.Optional.of(450), java.util.Optional.ofNullable(result.getHp()));
    Assert.assertEquals(java.util.Optional.of(1060L), java.util.Optional.ofNullable(result.getStartedAt()));
    Assert.assertEquals(java.util.Optional.of(1330L), java.util.Optional.ofNullable(result.getFinishedAt()));
  }

  @Test
  public void updateBuildingShouldReturnWithUpdatedFarm() {
    KingdomEntity kingdom = KingdomFactory.createKingdomEntityWithId(1L);
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereTownHallsLevelFive());
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithHighAmount());
    kingdom.setPlayer(PlayerFactory.createPlayer(1L, kingdom));
    BuildingEntity farm = kingdom.getBuildings().get(2);
    BuildingLevelDTO levelDTO = new BuildingLevelDTO(2);
    buildingService = Mockito.spy(buildingService);
    Mockito.doReturn(farm).when(buildingService).checkBuildingDetails(kingdom, farm.getId(), levelDTO);
    Mockito.when(buildingRepository.findById(farm.getId())).thenReturn(Optional.of(farm));
    Mockito.when(timeService.getTime()).thenReturn(1060L);
    Mockito.when(buildingRepository.save(any())).thenReturn(farm);
    Mockito.when(resourceService.hasResourcesForBuilding(kingdom, 200)).thenReturn(true);

    BuildingEntity result = buildingService.updateBuilding(kingdom, 3L, levelDTO);
    Assert.assertEquals(Optional.of(2), Optional.ofNullable(result.getLevel()));
    Assert.assertEquals(Optional.of(200), Optional.ofNullable(result.getHp()));
    Assert.assertEquals(Optional.of(1060L), Optional.ofNullable(result.getStartedAt()));
    Assert.assertEquals(Optional.of(1180L), Optional.ofNullable(result.getFinishedAt()));
  }

  @Test
  public void updateBuildingShouldReturnWithUpdatedMine() {
    KingdomEntity kingdom = KingdomFactory.createKingdomEntityWithId(1L);
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereTownHallsLevelFive());
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithHighAmount());
    kingdom.setPlayer(PlayerFactory.createPlayer(1L, kingdom));
    BuildingEntity mine = kingdom.getBuildings().get(3);
    BuildingLevelDTO levelDTO = new BuildingLevelDTO(4);
    buildingService = Mockito.spy(buildingService);
    Mockito.doReturn(mine).when(buildingService).checkBuildingDetails(kingdom, mine.getId(), levelDTO);
    Mockito.when(buildingRepository.findById(mine.getId())).thenReturn(Optional.of(mine));
    Mockito.when(timeService.getTime()).thenReturn(1060L);
    Mockito.when(buildingRepository.save(any())).thenReturn(mine);
    Mockito.when(resourceService.hasResourcesForBuilding(kingdom, 400)).thenReturn(true);

    BuildingEntity result = buildingService.updateBuilding(kingdom, 4L, levelDTO);
    Assert.assertEquals(Optional.of(4), Optional.ofNullable(result.getLevel()));
    Assert.assertEquals(Optional.of(400), Optional.ofNullable(result.getHp()));
    Assert.assertEquals(Optional.of(1060L), Optional.ofNullable(result.getStartedAt()));
    Assert.assertEquals(Optional.of(1300L), Optional.ofNullable(result.getFinishedAt()));
  }

  @Test(expected = IdNotFoundException.class)
  public void checkBuildingDetails_ShouldThrow_IdNotFound() {
    KingdomEntity kingdom = KingdomFactory.createFullKingdom(1L, 1L);

    Mockito.when(buildingRepository.findById(6L)).thenThrow(IdNotFoundException.class);

    BuildingEntity result = buildingService.checkBuildingDetails(kingdom, 6L, new BuildingLevelDTO(2));
  }

  @Test(expected = MissingParameterException.class)
  public void checkBuildingDetails_MissingParameterException_IfLevelDtoIsEmpty() {
    KingdomEntity kingdom = KingdomFactory.createFullKingdom(1L, 1L);

    Mockito.when(buildingRepository.findById(2L)).thenReturn(Optional.ofNullable(kingdom.getBuildings().get(1)));

    BuildingEntity result = buildingService.checkBuildingDetails(kingdom, 2L, new BuildingLevelDTO());
  }

  @Test(expected = MissingParameterException.class)
  public void checkBuildingDetails_MissingParameterException_IfLevelDtoIsEqualZero() {
    KingdomEntity kingdom = KingdomFactory.createFullKingdom(1L, 1L);

    Mockito.when(buildingRepository.findById(2L)).thenReturn(Optional.ofNullable(kingdom.getBuildings().get(1)));

    BuildingEntity result = buildingService.checkBuildingDetails(kingdom, 2L, new BuildingLevelDTO(0));
  }

  @Test(expected = MissingParameterException.class)
  public void checkBuildingDetails_MissingParameterException_IfLevelDtoIsNull() {
    KingdomEntity kingdom = KingdomFactory.createFullKingdom(1L, 1L);

    Mockito.when(buildingRepository.findById(2L)).thenReturn(Optional.ofNullable(kingdom.getBuildings().get(1)));

    BuildingEntity result = buildingService.checkBuildingDetails(kingdom, 2L, null);
  }

  @Test(expected = ForbiddenActionException.class)
  public void checkBuildingDetails_ForbiddenActionException_IfKingdomNotContainTheBuilding() {
    KingdomEntity kingdom = KingdomFactory.createFullKingdom(1L, 1L);
    KingdomEntity kingdom2 = KingdomFactory.createFullKingdom(1L, 1L);
    kingdom2.setBuildings(BuildingFactory.createBuildingsWhereBuildingsIdAre_5_8(kingdom2));

    Mockito.when(buildingRepository.findById(5L)).thenReturn(Optional.ofNullable(kingdom2.getBuildings().get(0)));

    BuildingEntity result = buildingService.checkBuildingDetails(kingdom, 5L, new BuildingLevelDTO(2));
  }

  @Test(expected = NotEnoughResourceException.class)
  public void checkBuildingDetails_NotEnoughResourceException() {
    KingdomEntity kingdom = KingdomFactory.createFullKingdom(1L, 1L);

    Mockito.when(buildingRepository.findById(2L)).thenReturn(Optional.ofNullable(kingdom.getBuildings().get(1)));
    Mockito.when(buildingRepository.findAllByKingdomId(kingdom.getId())).thenReturn(kingdom.getBuildings());

    BuildingEntity result = buildingService.checkBuildingDetails(kingdom, 2L, new BuildingLevelDTO(10));
  }

  @Test(expected = TownhallLevelException.class)
  public void checkBuildingDetails_TownHallLevelException() {
    KingdomEntity kingdom = KingdomFactory.createFullKingdom(1L, 1L);
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithHighAmount());

    Mockito.when(buildingRepository.findById(2L)).thenReturn(Optional.ofNullable(kingdom.getBuildings().get(1)));
    Mockito.when(buildingRepository.findAllByKingdomId(kingdom.getId())).thenReturn(kingdom.getBuildings());
    Mockito.when(resourceService.hasResourcesForBuilding(kingdom, 300)).thenReturn(true);

    BuildingEntity result = buildingService.checkBuildingDetails(kingdom, 2L, new BuildingLevelDTO(3));
  }

  @Test
  public void checkBuildingDetails_ShouldReturnTownHall_IfUpdateOwnBuilding() {
    KingdomEntity kingdom = KingdomFactory.createFullKingdom(1L, 1L);
    BuildingEntity townHall = kingdom.getBuildings().get(0);
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithHighAmount());

    Mockito.when(buildingRepository.findById(1L)).thenReturn(Optional.ofNullable(townHall));
    Mockito.when(buildingRepository.findAllByKingdomId(kingdom.getId())).thenReturn(kingdom.getBuildings());
    Mockito.when(resourceService.hasResourcesForBuilding(kingdom, 600)).thenReturn(true);

    BuildingEntity result = buildingService.checkBuildingDetails(kingdom, 1L, new BuildingLevelDTO(3));

    Assert.assertEquals(townHall, result);
  }

  @Test
  public void checkBuildingDetails_ShouldReturnTownHall_IfUserHasAdminRole() {
    KingdomEntity kingdom = KingdomFactory.createFullKingdom(1L, 1L);
    BuildingEntity townHall = kingdom.getBuildings().get(0);
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithHighAmount());
    KingdomEntity kingdom2 = KingdomFactory.createFullKingdom(1L, 1L);
    kingdom2.getPlayer().setRoleType(RoleType.ROLE_ADMIN);

    Mockito.when(buildingRepository.findById(1L)).thenReturn(Optional.ofNullable(townHall));
    Mockito.when(buildingRepository.findAllByKingdomId(kingdom2.getId())).thenReturn(kingdom2.getBuildings());
    Mockito.when(resourceService.hasResourcesForBuilding(kingdom, 600)).thenReturn(true);

    BuildingEntity result = buildingService.checkBuildingDetails(kingdom2, 1L, new BuildingLevelDTO(3));

    Assert.assertEquals(townHall, result);
  }

  @Test
  public void findBuildingByIdShouldReturnWithCorrectBuildingType() {
    BuildingEntity buildingEntity = new BuildingEntity(null, BuildingType.FARM, 0);

    Mockito.when(buildingRepository.findById(1L)).thenReturn(Optional.of(buildingEntity));

    String buildingType = buildingService.findBuildingById(1L).getType().toString();

    Assert.assertEquals("FARM", buildingType);
  }

  @Test
  public void findBuildingByIdShouldReturnWithUnCorrectBuildingType() {
    BuildingEntity buildingEntity = new BuildingEntity(null, BuildingType.MINE, 0);

    Mockito.when(buildingRepository.findById(1L)).thenReturn(Optional.of(buildingEntity));

    String buildingType = buildingService.findBuildingById(1L).getType().toString();

    Assert.assertNotEquals("FARM", buildingType);
  }

  @Test
  public void showBuildingShouldReturnCorrectBuildingDetails() {
    KingdomEntity kingdomEntity = new KingdomEntity();
    BuildingEntity buildingEntity = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 100L, 200L, null);
    List<BuildingEntity> fakeList = new ArrayList<>();
    fakeList.add(buildingEntity);
    kingdomEntity.setBuildings(fakeList);

    Mockito.when(buildingRepository.findById(1L)).thenReturn(Optional.of(buildingEntity));

    BuildingDetailsDTO result = buildingService.showBuilding(kingdomEntity, 1L);

    Assert.assertEquals(1, result.getId());
    Assert.assertEquals(1, result.getLevel());
    Assert.assertEquals("farm", result.getType());
    Assert.assertEquals(100, result.getHp());
  }

  @Test
  public void showBuildingShouldNotReturnWithBuildingDetails() {
    KingdomEntity kingdomEntity = new KingdomEntity();
    BuildingEntity buildingEntity = new BuildingEntity(2L, BuildingType.MINE, 2, 200, 100L, 200L, null);
    List<BuildingEntity> fakeList = new ArrayList<>();
    fakeList.add(buildingEntity);
    kingdomEntity.setBuildings(fakeList);

    Mockito.when(buildingRepository.findById(2L)).thenReturn(Optional.of(buildingEntity));

    BuildingDetailsDTO result = buildingService.showBuilding(kingdomEntity, 2L);

    Assert.assertNotEquals(1, result.getId());
    Assert.assertNotEquals(1, result.getLevel());
    Assert.assertNotEquals("farm", result.getType());
    Assert.assertNotEquals(100, result.getHp());
  }

  @Test(expected = IdNotFoundException.class)
  public void showBuildingShouldReturnWithIdNotFoundException() {
    KingdomEntity kingdomEntity = new KingdomEntity();
    BuildingEntity buildingEntity = new BuildingEntity(2L, BuildingType.MINE, 2, 200, 100L, 200L, null);
    List<BuildingEntity> fakeList = new ArrayList<>();
    fakeList.add(buildingEntity);
    kingdomEntity.setBuildings(fakeList);

    Mockito.when(buildingRepository.findById(3L)).thenReturn(Optional.empty());

    BuildingDetailsDTO response = buildingService.showBuilding(kingdomEntity, 3L);
  }

  @Test(expected = ForbiddenActionException.class)
  public void showBuildingShouldReturnWithForbiddenException() {
    BuildingEntity fakeBuilding2 = new BuildingEntity(3L, BuildingType.MINE, 2, 200, 100L, 200L, null);
    KingdomEntity kingdomEntity = new KingdomEntity();
    BuildingEntity fakeBuilding = new BuildingEntity(2L, BuildingType.MINE, 2, 200, 100L, 200L, null);
    List<BuildingEntity> fakeList = new ArrayList<>();
    fakeList.add(fakeBuilding);
    kingdomEntity.setBuildings(fakeList);

    Mockito.when(buildingRepository.findById(3L)).thenReturn(Optional.of(fakeBuilding2));

    BuildingDetailsDTO response = buildingService.showBuilding(kingdomEntity, 3L);
  }
}
