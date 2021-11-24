package com.greenfoxacademy.springwebapp.building.controllers;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsV2DTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsV3DTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingLevelDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingListResponseDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingDao;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidInputException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.TownhallLevelException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import com.greenfoxacademy.springwebapp.weapon.models.BuildingKindomBean;
import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(BuildingController.URI)
public class BuildingController {

    public static final String URI = "/kingdom/buildings";

    private final BuildingService buildingService;
    private final BuildingDao buildingDao;

    @PostMapping
    public ResponseEntity<?> buildBuilding(Authentication auth,
                                           @RequestBody @Valid BuildingRequestDTO dto)
            throws InvalidInputException, TownhallLevelException, NotEnoughResourceException {
        KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();
        return ResponseEntity.ok(buildingService.createBuilding(kingdom, dto));
    }

    @GetMapping("/buildingsByGenericSpecification")
    public ResponseEntity<List<BuildingDetailsDTO>> buildingsByGenericSpecification(
            @RequestParam(required = false) @Min(0) Long id,
            @RequestParam(required = false) @Min(0) Integer level,
            @RequestParam(required = false) @Min(0) Integer type,
            @RequestParam(required = false) @Min(0) Long startedAt)
            throws IdNotFoundException {

        List<BuildingDetailsDTO> list = buildingService.buildingsByGenericSpecification(id, level, type,
                startedAt);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/building-ids")
    public ResponseEntity<List<Long>> buildingsByGenericSpecification(
            @RequestParam(required = false) @Min(0) int size) {

        List<Long> list = buildingService.findAllIdsPaged(size);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBuildingById(@PathVariable Long id,
                                             Authentication auth)
            throws IdNotFoundException, ForbiddenActionException {

        KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();

        return ResponseEntity.ok(buildingService.showBuilding(kingdom, id));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<BuildingDetailsDTO>> getBuildingsByType(
            @PathVariable @Min(0) int type) {

        List<BuildingDetailsDTO> list = buildingService.getAllBuildingsByType(type);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/search")
    public ResponseEntity<List<BuildingDetailsDTO>> getBuildingsByType(
            @RequestParam @Min(0) Integer type,
            @RequestParam @Min(0) int level)
            throws IdNotFoundException {

        List<BuildingDetailsDTO> list = buildingService.getAllBuildingsByTypeLevelKingdomId(type,
                level);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping
    public ResponseEntity<BuildingListResponseDTO> getKingdomBuildings(Authentication auth) {
        KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();
        List<BuildingEntity> list = kingdom.getBuildings();
        return ResponseEntity.status(HttpStatus.OK).body(new BuildingListResponseDTO(list));
    }

    @GetMapping("/dao/getBean")
    public ResponseEntity<List<BuildingKindomBean>> getBean(Authentication auth, @RequestParam String buildingType, @RequestParam Long buildingId) {

        BuildingType type = Arrays.stream(BuildingType.values())
                .filter(t -> t.buildingType.equalsIgnoreCase(buildingType))
                .findFirst()
                .orElse(BuildingType.TOWNHALL);
        KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();

        return ResponseEntity.status(HttpStatus.OK).body(
                buildingDao.getSalePriceReportBeans(kingdom, buildingId, type, 0, 0, 0, 0));
    }

    @GetMapping("/dao/byParams")
    public ResponseEntity<List<BuildingDetailsDTO>> getBuildingsByRequestParams(
            Authentication auth, @RequestParam (required = false) Integer level, @RequestParam (required = false) Integer hp, @RequestParam (required = false) Long startedAt) {

        KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();

        return ResponseEntity.status(HttpStatus.OK).body(buildingDao.getBuildingsByRequestParams(level, hp, startedAt));
    }

    @GetMapping("/dao/byParamsInDtoProjections")
    public ResponseEntity<List<BuildingDetailsV2DTO>> getBuildingsByRequestParamsProjections(
            Authentication auth, @RequestParam (required = false) Integer level, @RequestParam (required = false) Integer hp, @RequestParam (required = false) Long startedAt) {

        KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();

        return ResponseEntity.status(HttpStatus.OK).body(buildingDao.getBuildingsByRequestParamsProjections(level, hp, startedAt));
    }

    @GetMapping("/dao/byParamsInDtoProjectionsJoin")
    public ResponseEntity<List<BuildingDetailsV3DTO>> getBuildingsByRequestParamsProjectionsJoin(
            Authentication auth, @RequestParam (required = false) Integer level, @RequestParam (required = false) Integer hp, @RequestParam (required = false) Long startedAt,@RequestParam(required = false) String kingdomName) {

        KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();

        return ResponseEntity.status(HttpStatus.OK).body(buildingDao.getBuildingsByRequestParamsProjectionsJoin(level, hp, startedAt,kingdomName));
    }

    @GetMapping("/dao/byParamsInDtoV2")
    public ResponseEntity<List<BuildingDetailsV2DTO>> getBuildingsByRequestParamsInDtoV2(
            Authentication auth, @RequestParam (required = false) Integer level, @RequestParam (required = false) Integer hp, @RequestParam (required = false) Long startedAt) {

        KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();

        return ResponseEntity.status(HttpStatus.OK).body(buildingDao.getBuildingsByRequestParamsInDtoV2(level, hp, startedAt));
    }

    @GetMapping("/dao/byParamsInDtoV1")
    public ResponseEntity<List<BuildingDetailsDTO>> getBuildingsByRequestParamsInDtoV1(
            Authentication auth, @RequestParam (required = false) Integer level, @RequestParam (required = false) Integer hp, @RequestParam (required = false) Long startedAt) {

        KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();

        return ResponseEntity.status(HttpStatus.OK).body(buildingDao.getBuildingsByRequestParamsInDtoV1(level, hp, startedAt));
    }

    @GetMapping("/dao/byParamsString")
    public ResponseEntity<List<BuildingDetailsDTO>> getBuildingsByRequestParamsString(
            Authentication auth, @RequestParam (required = false) Integer level, @RequestParam (required = false) Integer hp, @RequestParam (required = false) Long startedAt) {

        KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();

        return ResponseEntity.status(HttpStatus.OK).body(buildingDao.getBuildingsByRequestParams(level, hp, startedAt));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTheGivenBuildingDetails(@PathVariable Long id,
                                                           Authentication auth,
                                                           @RequestBody BuildingLevelDTO level)
            throws IdNotFoundException, MissingParameterException, TownhallLevelException, NotEnoughResourceException {

        KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();

        return ResponseEntity.ok().body(buildingService.updateBuilding(kingdom, id, level));
    }

}
