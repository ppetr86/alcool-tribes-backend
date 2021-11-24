package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsV2DTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsV3DTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.weapon.models.BuildingKindomBean;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class BuildingDaoImpl implements BuildingDao {

    private final BuildingRepository repo;
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    private CriteriaBuilder criteriaBuilder;
    private EntityManager entityManager;


    public BuildingDaoImpl(BuildingRepository repo, EntityManagerFactory entityManagerFactory) {
        this.repo = repo;
        this.entityManagerFactory = entityManagerFactory;
        this.entityManager = entityManagerFactory.createEntityManager();
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    @Transactional(readOnly = true)
    @Override
    public List<BuildingKindomBean> getSalePriceReportBeans(KingdomEntity kingdom, long id, BuildingType type, int level, int hp, long startedAt, long finishedAt) {

        CriteriaQuery<BuildingKindomBean> query = criteriaBuilder.createQuery(BuildingKindomBean.class);
        final Root<BuildingEntity> fromBuilding = query.from(BuildingEntity.class);
        final Join<BuildingEntity, KingdomEntity> pathKingdom = fromBuilding.join("kingdom", JoinType.LEFT);

        final Path<String> pathKingdomName = pathKingdom.get("kingdomName");
        final Path<Long> pathKingdomId = pathKingdom.get("id");
        final Path<BuildingType> pathBuildingType = fromBuilding.get("type");
        final Path<Long> pathBuildingId = fromBuilding.get("id");

        query.multiselect(pathKingdomName, pathKingdomId, pathBuildingType, pathBuildingId);

        final TypedQuery<BuildingKindomBean> typedQuery = entityManager.createQuery(query);

        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<BuildingDetailsDTO> getBuildingsByRequestParams(Integer level, Integer hp, Long startedAt) {

        CriteriaQuery<BuildingEntity> query = criteriaBuilder.createQuery(BuildingEntity.class);
        final Root<BuildingEntity> root = query.from(BuildingEntity.class);

        if (level != null && level > 0)
            criteriaBuilder.greaterThanOrEqualTo(root.get("level"), level);

        if (hp != null && hp > 0)
            criteriaBuilder.greaterThanOrEqualTo(root.get("hp"), hp);

        query.orderBy(criteriaBuilder.asc(root.get("id")));
        TypedQuery<BuildingEntity> tq = entityManager.createQuery(query);
        List<BuildingEntity> temp = tq.getResultList();
        //entityManager.close();
        return temp.stream()
                .map(BuildingDetailsDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<BuildingDetailsV2DTO> getBuildingsByRequestParamsProjections(Integer level, Integer hp, Long startedAt) {
        //https://thorben-janssen.com/dto-projections/
        CriteriaQuery<BuildingDetailsV2DTO> query = criteriaBuilder.createQuery(BuildingDetailsV2DTO.class);
        final Root<BuildingEntity> root = query.from(BuildingEntity.class);

        query.select(criteriaBuilder.construct(BuildingDetailsV2DTO.class,
                root.get("id"),
                root.get("level"),
                root.get("hp"),
                root.get("startedAt"),
                root.get("finishedAt")));

        if (level != null && level > 0)
            query.where(criteriaBuilder.greaterThanOrEqualTo(root.get("level"), level));

        if (hp != null && hp > 0)
            query.where(criteriaBuilder.greaterThanOrEqualTo(root.get("hp"), hp));

        query.orderBy(criteriaBuilder.asc(root.get("id")));
        TypedQuery<BuildingDetailsV2DTO> tq = entityManager.createQuery(query);
        List<BuildingDetailsV2DTO> temp = tq.getResultList();
        return temp;
    }

    @Override
    public List<BuildingDetailsV2DTO> getBuildingsByRequestParamsInDtoV2(Integer level, Integer hp, Long startedAt) {

        List<BuildingDetailsV2DTO> temp = repo.getBuildingDetailsDtoV2(level, hp, startedAt);
        return temp;
    }

    @Override
    public List<BuildingDetailsDTO> getBuildingsByRequestParamsInDtoV1(Integer level, Integer hp, Long startedAt) {

        List<BuildingDetailsDTO> temp = repo.getBuildingDetailsDtoV1(level, hp, startedAt);
        return temp;
    }

    @Transactional(readOnly = true)
    @Override
    public List<BuildingDetailsDTO> getBuildingsByRequestParamsString(Integer level, Integer hp, Long startedAt) {

        String stringQuery = "select b from BuildingEntity b";
        if (level != null && hp != null & startedAt != null)
            stringQuery += " where";

        if (level != null)
            stringQuery += " b.level >= :level";
        if (hp != null)
            stringQuery += " AND b.hp >= :hp";
        if (startedAt != null)
            stringQuery += " AND b.hp >= :startedAt";

        Query q = entityManager.createQuery(stringQuery);
        q.setParameter("level", level);
        q.setParameter("hp", hp);
        q.setParameter("startedAt", startedAt);
        List<BuildingEntity> temp = (List<BuildingEntity>) q.getResultList();
        entityManager.close();
        return temp.stream()
                .map(BuildingDetailsDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<BuildingDetailsV3DTO> getBuildingsByRequestParamsProjectionsJoin(Integer level, Integer hp, Long startedAt, String kingdomName) {

        CriteriaQuery<BuildingDetailsV3DTO> query = criteriaBuilder.createQuery(BuildingDetailsV3DTO.class);

        Root<BuildingEntity> root = query.from(BuildingEntity.class);
        Join<BuildingEntity, KingdomEntity> join = root.join("kingdom", JoinType.LEFT);

        Path<Long> idPath = root.get("id");
        Path<Integer> levelPath = root.get("level");
        Path<Integer> hpPath = root.get("hp");
        Path<Long> startedAtPath = root.get("startedAt");
        Path<Long> finishedAtPath = root.get("finishedAt");
        Path<String> kingdomNamePath = join.get("kingdomName");

        Predicate levelPred = criteriaBuilder.greaterThanOrEqualTo(root.get("level"), level);
        Predicate hpPred = criteriaBuilder.greaterThanOrEqualTo(root.get("hp"), hp);
        Predicate kingdomNamePred = criteriaBuilder.like(join.get("kingdomName"), kingdomName);
        Predicate conjunction = criteriaBuilder.and(levelPred, hpPred, kingdomNamePred);

        query.multiselect(criteriaBuilder.construct(BuildingDetailsV3DTO.class, idPath, levelPath, hpPath, startedAtPath, finishedAtPath, kingdomNamePath));
        query = query.where(conjunction);
        query.orderBy(criteriaBuilder.asc(root.get("id")));
        TypedQuery<BuildingDetailsV3DTO> tq = entityManager.createQuery(query);
        List<BuildingDetailsV3DTO> temp = tq.getResultList();
        return temp;
    }

}





















