package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingLevelDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidInputException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.TownhallLevelException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.enums.RoleType;
import com.greenfoxacademy.springwebapp.resource.models.enums.ResourceType;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import com.greenfoxacademy.springwebapp.specification.SpecificationFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class BuildingServiceImpl implements BuildingService {

    private final Environment env;
    private final BuildingRepository repo;
    private final TimeService timeService;
    private final ResourceService resourceService;
    private final SpecificationFactory<BuildingEntity> specificationFactory;


    @Transactional(readOnly = true)
    @Override
    public List<BuildingDetailsDTO> getBuildingsByRequestParams(Integer level, Integer hp, Long startedAt) {

        Specification<BuildingEntity> levelSpec = null;
        if (level != null && level > 0)
            levelSpec = specificationFactory.isGreaterThan("level", level);

        Specification<BuildingEntity> hpSpec = null;
        if (hp != null && hp > 0)
            hpSpec = specificationFactory.isGreaterThan("hp", hp);

        Specification<BuildingEntity> startedAtSpec = null;
        if (hp != null && hp > 0)
            startedAtSpec = specificationFactory.isGreaterThan("startedAt", startedAt);

        Specification<BuildingEntity> spec = Specification.where(levelSpec)
                .and(hpSpec)
                .and(startedAtSpec);

        return repo.findAll(spec).stream()
                .map(BuildingDetailsDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BuildingDetailsDTO> buildingsByGenericSpecification(Long id, Integer level,
                                                                    Integer type, Long startedAt) {

        Specification<BuildingEntity> idGreaterThan = specificationFactory.isGreaterThan("id", id);
        Specification<BuildingEntity> levelGreaterThan = specificationFactory.isGreaterThan("level",
                level);
        BuildingType type1 = convertOrdinalBuildingType(type);
        Specification<BuildingEntity> typeEquals = specificationFactory.isEqual("type", type1);
        Specification<BuildingEntity> startedAtIsGreater = specificationFactory.isGreaterThan(
                "startedAt", startedAt);

        Specification<BuildingEntity> spec =
                Specification.where(idGreaterThan)
                        .and(levelGreaterThan)
                        .and(typeEquals)
                        .and(startedAtIsGreater);

        return repo.findAll(spec).stream().map(BuildingDetailsDTO::new).collect(Collectors.toList());

    }

    @Override
    public List<BuildingDetailsDTO> buildingsByGenericSpecificationPaged(Long id, int pageNr,
                                                                         int pageSize) {
        Pageable pageRequest = createPageRequest(pageNr, pageSize);
        Specification<BuildingEntity> spec = specificationFactory.isGreaterThan("id", id);
        return repo.findAll(spec, pageRequest).stream().map(BuildingDetailsDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BuildingDetailsDTO> buildingsByGenericSpecificationSorted(Long id, String sort) {
        Direction sortDir = sortBy(sort);
        Specification<BuildingEntity> spec = specificationFactory.isGreaterThan("id", id);
        return repo.findAll(spec, Sort.by(sortDir, "id")).stream().map(BuildingDetailsDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public BuildingEntity checkBuildingDetails(KingdomEntity kingdom, Long id,
                                               BuildingLevelDTO levelDTO)
            throws IdNotFoundException, MissingParameterException, TownhallLevelException,
            NotEnoughResourceException, ForbiddenActionException {
        BuildingEntity building = findBuildingById(id);
        if (building == null) {
            throw new IdNotFoundException();
        }
        if (levelDTO == null || levelDTO.getLevel() == 0) {
            throw new MissingParameterException("level");
        }
        if (!(findBuildingsByKingdomId(kingdom.getId()).contains(building)
                || kingdom.getPlayer().getRoleType().equals(RoleType.ROLE_ADMIN))) {
            throw new ForbiddenActionException();
        }

        hasEnoughResourceForBuild(building, levelDTO);

        if (building.getType().equals(BuildingType.TOWNHALL)) {
            return building;
        }
        BuildingEntity townHall = getTownHallFromKingdom(building.getKingdom());
        if (townHall.getLevel() < levelDTO.getLevel()) {
            throw new TownhallLevelException();
        }

        return building;
    }

    @Override
    public BuildingEntity createBuilding(KingdomEntity kingdom, BuildingRequestDTO dto)
            throws InvalidInputException, TownhallLevelException, NotEnoughResourceException {

        if (!isBuildingTypeInRequestOk(dto)) {
            throw new InvalidInputException("building type");
        }
        if (!hasKingdomTownhall(kingdom)) {
            throw new TownhallLevelException();
        }
        int amountChange = defineBuildingFirstLevelCosts(dto.getType());
        if (!resourceService.hasResourcesForBuilding(kingdom, amountChange)) {
            throw new NotEnoughResourceException();
        }

        resourceService.updateResourceAmount(kingdom, -(amountChange), ResourceType.GOLD);
        BuildingEntity result = setBuildingTypeOnEntity(dto.getType());
        result.setStartedAt(timeService.getTime());
        result.setKingdom(kingdom);
        result.setLevel(1);
        result = defineFinishedAt(result);
        result = defineHp(result);
        result = save(result);
        resourceService.updateResourceGeneration(kingdom, result);

        return result;
    }

    @Override
    public List<BuildingEntity> createDefaultBuildings(KingdomEntity kingdom) {
        return Arrays.stream(BuildingType.values())
                .map(type -> new BuildingEntity(kingdom,
                        type,
                        1,
                        Integer.parseInt(env.getProperty(String.format("building.%s.hp",
                                type.toString().toLowerCase()))),
                        timeService.getTime(),
                        timeService.getTime()))
                .collect(Collectors.toList());
    }

    public int defineBuildingCosts(String buildingType) {
        return Integer.parseInt(
                Objects.requireNonNull(env.getProperty(String.format("building.%s.buildingCosts",
                        buildingType.toLowerCase()))));
    }

    public int defineBuildingFirstLevelCosts(String buildingType) {
        return Integer.parseInt(
                Objects.requireNonNull(env.getProperty(String.format("building.%s.buildingCosts.firstLevel",
                        buildingType.toLowerCase()))));
    }

    @Override
    public BuildingEntity defineFinishedAt(BuildingEntity entity) {
        String time =
                env.getProperty(
                        String.format("building.%s.buildingTime", entity.getType().buildingType.toLowerCase()));
        entity.setFinishedAt(entity.getStartedAt() + Long.parseLong(time));
        return entity;
    }

    @Override
    public BuildingEntity defineHp(BuildingEntity entity) {
        String hp = env.getProperty(
                String.format("building.%s.hp", entity.getType().buildingType.toLowerCase()));
        entity.setHp(Integer.parseInt(hp));
        return entity;
    }

    @Override
    public List<Long> findAllIdsPaged(int size) {
        return repo.findAllIdsPaged(PageRequest.of(0, size));
    }

    @Override
    public BuildingEntity findBuildingById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public BuildingEntity findBuildingWithHighestLevel(KingdomEntity kingdom,
                                                       BuildingType buildingType) {
        List<BuildingEntity> buildings = kingdom.getBuildings().stream()
                .filter(a -> a.getType() == buildingType)
                .collect(Collectors.toList());
        return buildings.stream()
                .max((building1, building2) -> building1.getLevel() > building2.getLevel() ? 1 : -1)
                .orElse(null);
    }

    @Override
    public List<BuildingEntity> findBuildingsByKingdomId(Long id) {
        return repo.findAllByKingdomId(id);
    }

    @Override
    public List<BuildingDetailsDTO> getAllBuildingsByType(int buildingType) {
        return repo.findAll(buildingsByType(buildingType)).stream().map(BuildingDetailsDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BuildingDetailsDTO> getAllBuildingsByTypeLevelKingdomId(Integer type, int level) {
        Specification<BuildingEntity> spec =
                Specification.where(buildingsByType(type))
                        .and(buildingsByLevel(level));

        return repo.findAll(spec).stream().map(BuildingDetailsDTO::new).collect(Collectors.toList());
    }

    @Override
    public boolean hasKingdomTownhall(KingdomEntity kingdom) {
        if (kingdom.getBuildings() == null) {
            return false;
        }
        return kingdom.getBuildings().stream()
                .anyMatch(building -> building.getType().equals(BuildingType.TOWNHALL));
    }

    @Override
    public boolean isBuildingTypeInRequestOk(BuildingRequestDTO dto) {
        try {
            BuildingType.valueOf(dto.getType().toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public BuildingEntity save(BuildingEntity entity) {
        return repo.save(entity);
    }

    @Override
    public BuildingEntity setBuildingTypeOnEntity(String type) {
        BuildingEntity building = new BuildingEntity();

        try {
            BuildingType.valueOf(type.toUpperCase());
            building.setType(BuildingType.valueOf(type.toUpperCase()));
            return building;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public BuildingDetailsDTO showBuilding(KingdomEntity kingdom, Long id)
            throws IdNotFoundException, ForbiddenActionException {

        BuildingEntity myBuilding = kingdom.getBuildings().stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (myBuilding == null) {
            BuildingEntity actualBuilding = findBuildingById(id);
            if (actualBuilding == null) {
                throw new IdNotFoundException();
            } else {
                throw new ForbiddenActionException();
            }
        }
        return new BuildingDetailsDTO(myBuilding);
    }

    @Override
    public BuildingEntity updateBuilding(KingdomEntity kingdom, Long id, BuildingLevelDTO levelDTO)
            throws IdNotFoundException, MissingParameterException, TownhallLevelException, NotEnoughResourceException {

        BuildingEntity updatedBuilding = checkBuildingDetails(kingdom, id, levelDTO);

        int buildingHp = fetchBuildingSetting(updatedBuilding.getType(), "hp");
        int buildingTime = fetchBuildingSetting(updatedBuilding.getType(), "buildingTime");

        updatedBuilding.setLevel(levelDTO.getLevel());
        updatedBuilding.setHp(levelDTO.getLevel() * buildingHp);
        updatedBuilding.setStartedAt(timeService.getTime());
        updatedBuilding.setFinishedAt(
                updatedBuilding.getStartedAt() + (levelDTO.getLevel() * buildingTime));
        int cost = fetchBuildingSetting(updatedBuilding.getType(), "buildingCosts");
        int amountChange = cost * levelDTO.getLevel();
        resourceService.updateResourceAmount(updatedBuilding.getKingdom(), -(amountChange),
                ResourceType.GOLD);
        BuildingEntity proxied = repo.save(updatedBuilding);
        BuildingEntity unproxied = Hibernate.unproxy(proxied, BuildingEntity.class);
        return unproxied;
    }

    private Specification<BuildingEntity> buildingsByKingdomId(int kingdomId) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("kingdomId"))
                .value((long) kingdomId)));
    }

    private Specification<BuildingEntity> buildingsByLevel(int level) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("level")).value(level));
    }

    private Specification<BuildingEntity> buildingsByType(Integer type) {
        if (type == null) {
            return (((root, query, criteriaBuilder) -> criteriaBuilder.isTrue(
                    criteriaBuilder.literal(true))));
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("type")).value(type));
    }

    private BuildingType convertOrdinalBuildingType(Integer type) {
        BuildingType[] values = BuildingType.values();
        return type == null || type > values.length - 1 ? values[0] : values[type];
    }

    private Pageable createPageRequest(int pageNr, int pageSize) {
        return PageRequest.of(pageNr, pageSize, Direction.ASC, "id");
    }

    private int fetchBuildingSetting(BuildingType buildingType, String setting) {
        return Integer
                .parseInt(Objects.requireNonNull(
                        env.getProperty(String.format("building.%s.%s", buildingType.buildingType.toLowerCase(),
                                setting))));
    }

    private BuildingEntity getTownHallFromKingdom(KingdomEntity kingdom) {
        return kingdom.getBuildings().stream()
                .filter(building -> building.getType().equals(BuildingType.TOWNHALL))
                .findFirst()
                .get();
    }

    private void hasEnoughResourceForBuild(BuildingEntity building, BuildingLevelDTO levelDTO)
            throws NotEnoughResourceException {

        int cost = fetchBuildingSetting(building.getType(), "buildingCosts");
        int amountChange = cost * levelDTO.getLevel();

        if (!resourceService.hasResourcesForBuilding(building.getKingdom(), amountChange)) {
            throw new NotEnoughResourceException();
        }
    }

    private Direction sortBy(String sort) {
        return sort.equalsIgnoreCase("desc") ? Direction.DESC : Direction.ASC;
    }
}


