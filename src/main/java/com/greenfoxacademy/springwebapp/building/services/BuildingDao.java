package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsV2DTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsV3DTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.weapon.models.BuildingKindomBean;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface BuildingDao {


    List<BuildingKindomBean> getSalePriceReportBeans(KingdomEntity kingdom, long id, BuildingType type, int level, int hp, long startedAt, long finishedAt);


    List<BuildingDetailsDTO> getBuildingsByRequestParams(Integer level, Integer hp, Long startedAt);


    @Transactional(readOnly = true)
    List<BuildingDetailsV2DTO> getBuildingsByRequestParamsProjections(Integer level, Integer hp, Long startedAt);


    List<BuildingDetailsV2DTO> getBuildingsByRequestParamsInDtoV2(Integer level, Integer hp, Long startedAt);


    List<BuildingDetailsDTO> getBuildingsByRequestParamsInDtoV1(Integer level, Integer hp, Long startedAt);


    @Transactional(readOnly = true)
    List<BuildingDetailsDTO> getBuildingsByRequestParamsString(Integer level, Integer hp, Long startedAt);


    @Transactional(readOnly = true)
    List<BuildingDetailsV3DTO> getBuildingsByRequestParamsProjectionsJoin(Integer level, Integer hp, Long startedAt, String kingdomName);
}
