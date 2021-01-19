package com.greenfoxacademy.springwebapp.buildings;

import com.greenfoxacademy.springwebapp.buildings.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.buildings.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.buildings.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.buildings.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.buildings.services.BuildingService;
import com.greenfoxacademy.springwebapp.buildings.services.BuildingServiceImpl;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BuildingServiceTest {

  private BuildingRepository buildingRepository;
  private BuildingService buildingService;
  private TimeService timeService;

  @Before
  public void init() {
    buildingService = new BuildingServiceImpl(buildingRepository, timeService);
  }

  @Test
  public void defineFinishedAt_Correct_Townhall(){
    //arrange
    BuildingEntity b = new BuildingEntity(BuildingType.TOWNHALL, 0L);
    //act
    buildingService.defineFinishedAt(b);
    // assert
    Assert.assertEquals(120L, b.getFinishedAt());
  }

  @Test
  public void defineFinishedAt_Correct_Farm(){
    //arrange
    BuildingEntity b = new BuildingEntity(BuildingType.FARM, 0L);
    //act
    buildingService.defineFinishedAt(b);
    // assert
    Assert.assertEquals(100, b.getFinishedAt());
  }
  @Test
  public void defineFinishedAt_Correct_Mine(){
    //arrange
    BuildingEntity b = new BuildingEntity(BuildingType.MINE, 0L);
    //act
    buildingService.defineFinishedAt(b);
    // assert
    Assert.assertEquals(100, b.getFinishedAt());
  }

  @Test
  public void defineFinishedAt_Correct_Academy(){
    //arrange
    BuildingEntity b = new BuildingEntity(BuildingType.ACADEMY, 0L);
    //act
    buildingService.defineFinishedAt(b);
    // assert
    Assert.assertEquals(90, b.getFinishedAt());
  }

  @Test
  public void defineFinishedAt_Wrong_Townhall(){
    //arrange
    BuildingEntity b = new BuildingEntity(BuildingType.TOWNHALL, 0L);
    //act
    buildingService.defineFinishedAt(b);
    // assert
    Assert.assertNotEquals(119L, b.getFinishedAt());
  }

  @Test
  public void defineFinishedAt_Wrong_Farm(){
    //arrange
    BuildingEntity b = new BuildingEntity(BuildingType.FARM, 0L);
    //act
    buildingService.defineFinishedAt(b);
    // assert
    Assert.assertNotEquals(99, b.getFinishedAt());
  }
  @Test
  public void defineFinishedAt_Wrong_Mine(){
    //arrange
    BuildingEntity b = new BuildingEntity(BuildingType.MINE, 0L);
    //act
    buildingService.defineFinishedAt(b);
    // assert
    Assert.assertNotEquals(101, b.getFinishedAt());
  }

  @Test
  public void defineFinishedAt_Wrong_Academy(){
    //arrange
    BuildingEntity b = new BuildingEntity(BuildingType.ACADEMY, 0L);
    //act
    buildingService.defineFinishedAt(b);
    // assert
    Assert.assertNotEquals(89, b.getFinishedAt());
  }

  @Test
  public void isTypeOkRequest_Townhall_ShouldTrue_VariousCase(){
    BuildingRequestDTO dto = new BuildingRequestDTO("ToWNhall");
    BuildingEntity building = buildingService.createBuildingType(dto.getType());
    Assert.assertEquals(BuildingType.TOWNHALL, building.getType());
  }

  @Test
  public void isTypeOkRequest_Townhall_ShouldTrue_LowerCase(){
    BuildingRequestDTO dto = new BuildingRequestDTO("townhall");
    BuildingEntity building = buildingService.createBuildingType(dto.getType());
    Assert.assertEquals(BuildingType.TOWNHALL, building.getType());
  }

  @Test
  public void isTypeOkRequest_Townhall_ShouldTrue_UpperCase(){
    BuildingRequestDTO dto = new BuildingRequestDTO("TOWNHALL");
    BuildingEntity building = buildingService.createBuildingType(dto.getType());
    Assert.assertEquals(BuildingType.TOWNHALL, building.getType());
  }

  @Test
  public void isTypeOkRequest_Townhall_ShouldNull_UpperCase(){
    BuildingRequestDTO dto = new BuildingRequestDTO("TOWNHALLL");
    BuildingEntity building = buildingService.createBuildingType(dto.getType());
    Assert.assertNull(building.getType());
  }

  @Test
  public void isTypeOkRequest_Farm_ShouldTrue_UpperCase(){
    BuildingRequestDTO dto = new BuildingRequestDTO("FARM");
    BuildingEntity building = buildingService.createBuildingType(dto.getType());
    Assert.assertEquals(BuildingType.FARM, building.getType());
  }

  @Test
  public void isTypeOkRequest_Mine_ShouldTrue_LowerCase(){
    BuildingRequestDTO dto = new BuildingRequestDTO("mine");
    BuildingEntity building = buildingService.createBuildingType(dto.getType());
    Assert.assertEquals(BuildingType.MINE, building.getType());
  }

  @Test
  public void isTypeOkRequest_Academy_ShouldTrue_VariousCase(){
    BuildingRequestDTO dto = new BuildingRequestDTO("ACAdemy");
    BuildingEntity building = buildingService.createBuildingType(dto.getType());
    Assert.assertEquals(BuildingType.ACADEMY, building.getType());
  }
}
