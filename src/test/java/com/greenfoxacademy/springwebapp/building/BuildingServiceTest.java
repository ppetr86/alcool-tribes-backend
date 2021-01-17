package com.greenfoxacademy.springwebapp.building;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.building.services.BuildingServiceImpl;
import com.greenfoxacademy.springwebapp.commonServices.TimeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BuildingServiceTest {

  private BuildingService buildingService;

  @Before
  public void init() {
    BuildingRepository buildingRepository = Mockito.mock(BuildingRepository.class);
    TimeService timeService = Mockito.mock(TimeService.class);
    buildingService = new BuildingServiceImpl(buildingRepository, timeService);
  }

  @Test
  public void defineFinishedAt_Correct_Townhall(){
    BuildingEntity b = new BuildingEntity(BuildingType.TOWNHALL, 0L);
    buildingService.defineFinishedAt(b);
    Assert.assertEquals(120L, b.getFinishedAt());
  }

  @Test
  public void defineFinishedAt_Correct_Farm(){
    BuildingEntity b = new BuildingEntity(BuildingType.FARM, 0L);
    buildingService.defineFinishedAt(b);
    Assert.assertEquals(60, b.getFinishedAt());
  }
  @Test
  public void defineFinishedAt_Correct_Mine(){
    BuildingEntity b = new BuildingEntity(BuildingType.MINE, 0L);
    buildingService.defineFinishedAt(b);
    Assert.assertEquals(60, b.getFinishedAt());
  }

  @Test
  public void defineFinishedAt_Correct_Academy(){
    BuildingEntity b = new BuildingEntity(BuildingType.ACADEMY, 0L);
    buildingService.defineFinishedAt(b);
    Assert.assertEquals(90, b.getFinishedAt());
  }

  @Test
  public void defineFinishedAt_Wrong_Townhall(){
    BuildingEntity b = new BuildingEntity(BuildingType.TOWNHALL, 0L);
    buildingService.defineFinishedAt(b);
    Assert.assertNotEquals(119L, b.getFinishedAt());
  }

  @Test
  public void defineFinishedAt_Wrong_Farm(){
    BuildingEntity b = new BuildingEntity(BuildingType.FARM, 0L);
    buildingService.defineFinishedAt(b);
    Assert.assertNotEquals(99, b.getFinishedAt());
  }
  @Test
  public void defineFinishedAt_Wrong_Mine(){
    BuildingEntity b = new BuildingEntity(BuildingType.MINE, 0L);
    buildingService.defineFinishedAt(b);
    Assert.assertNotEquals(101, b.getFinishedAt());
  }

  @Test
  public void defineFinishedAt_Wrong_Academy(){
    BuildingEntity b = new BuildingEntity(BuildingType.ACADEMY, 0L);
    buildingService.defineFinishedAt(b);
    Assert.assertNotEquals(89, b.getFinishedAt());
  }

  @Test
  public void isTypeOkRequest_Townhall_ShouldTrue_VariousCase(){
    Assert.assertEquals(BuildingType.TOWNHALL, buildingService.createBuildingType("ToWNhall").getType());
  }

  @Test
  public void isTypeOkRequest_Townhall_ShouldTrue_LowerCase(){
    Assert.assertEquals(BuildingType.TOWNHALL, buildingService.createBuildingType("townhall").getType());
  }

  @Test
  public void isTypeOkRequest_Townhall_ShouldTrue_UpperCase(){
    Assert.assertEquals(BuildingType.TOWNHALL, buildingService.createBuildingType("TOWNHALL").getType());
  }

  @Test
  public void isTypeOkRequest_Townhall_ShouldNull_UpperCase(){
    Assert.assertNull(buildingService.createBuildingType("TOWNHALLL").getType());
  }

  @Test
  public void isTypeOkRequest_Farm_ShouldTrue_UpperCase(){
    Assert.assertEquals(BuildingType.FARM, buildingService.createBuildingType("FARM").getType());
  }

  @Test
  public void isTypeOkRequest_Mine_ShouldTrue_LowerCase(){
    Assert.assertEquals(BuildingType.MINE,
            buildingService.createBuildingType("mine").getType());
  }

  @Test
  public void isTypeOkRequest_Academy_ShouldTrue_VariousCase(){
    Assert.assertEquals(BuildingType.ACADEMY,
            buildingService.createBuildingType("ACAdemy").getType());
  }
}
