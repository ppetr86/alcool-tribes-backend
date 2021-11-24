package com.greenfoxacademy.springwebapp.weapon.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.weapon.models.ColdWeaponEntity;
import com.greenfoxacademy.springwebapp.weapon.models.MachineGunEntity;
import com.greenfoxacademy.springwebapp.weapon.models.PistolEntity;
import com.greenfoxacademy.springwebapp.weapon.models.SemiAutomaticGunEntity;
import com.greenfoxacademy.springwebapp.weapon.models.ShotGunEntity;
import com.greenfoxacademy.springwebapp.weapon.models.SniperRifleEntity;
import com.greenfoxacademy.springwebapp.weapon.models.WeaponEntity;
import com.greenfoxacademy.springwebapp.weapon.models.WeaponType;
import com.greenfoxacademy.springwebapp.weapon.models.WeaponsDTO;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WeaponServiceImpl implements WeaponService {

    @Override
    public Set<WeaponEntity> addWeaponsToKingdom(KingdomEntity kingdom, WeaponsDTO... weapons) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Set<WeaponEntity> newWeapons = new HashSet<>(weapons.length);
        Query query;
        for (WeaponsDTO weaponDTO : weapons) {
            UUID id = generateUniqueUUID();
            query = entityManager.createNativeQuery("INSERT INTO weapons " +
                    "(name, type, fk_kingdom, magazine_size, is_automaticable, is_semiautoable, is_concealable, shooting_distance, blade_length, discriminator, id)" +
                    "VALUES (?,?,?,?,?,?,?,?,?,?, ?)");
            query.setParameter(1, weaponDTO.getName());
            query.setParameter(2, weaponDTO.getType());
            query.setParameter(3, kingdom);
            query.setParameter(10,id );

            if (weaponDTO.getType().equals(WeaponType.SEMI_AUTOMATIC_GUN)) {
                SemiAutomaticGunEntity casted =  new SemiAutomaticGunEntity(weaponDTO.getName(), weaponDTO.getType(), kingdom,id );
                query.setParameter(4, casted.getMagazineSize());
                query.setParameter(5, casted.getIsAutomaticable());
            } else if (weaponDTO.getType().equals(WeaponType.COLD_WEAPON)) {
                ColdWeaponEntity casted = new ColdWeaponEntity(weaponDTO.getName(), weaponDTO.getType(), kingdom,id );
                query.setParameter(9, casted.getBladeLength());
            } else if (weaponDTO.getType().equals(WeaponType.MACHINE_GUN)) {
                MachineGunEntity casted = new MachineGunEntity(weaponDTO.getName(), weaponDTO.getType(), kingdom,id );
                query.setParameter(4, casted.getMagazineSize());
            } else if (weaponDTO.getType().equals(WeaponType.PISTOL)) {
                PistolEntity casted = new PistolEntity(weaponDTO.getName(), weaponDTO.getType(), kingdom,id );
                query.setParameter(4, casted.getMagazineSize());
                query.setParameter(7, casted.getIsConcealable());
            } else if (weaponDTO.getType().equals(WeaponType.SHOTGUN)) {
                ShotGunEntity casted = new ShotGunEntity(weaponDTO.getName(), weaponDTO.getType(), kingdom,id );
                query.setParameter(4, casted.getMagazineSize());
            } else if (weaponDTO.getType().equals(WeaponType.SNIPER_RIFLE)) {
                SniperRifleEntity casted = new SniperRifleEntity(weaponDTO.getName(), weaponDTO.getType(), kingdom,id );
                query.setParameter(4, casted.getMagazineSize());
                query.setParameter(8, casted.getMagazineSize());
            }
            query.executeUpdate();
        }

        entityManager.close();
        entityManagerFactory.close();

        return newWeapons;
    }

    @Override
    public WeaponEntity changeWeaponName(KingdomEntity kingdom, WeaponEntity weapon) {
        return null;
    }

    @Override
    public WeaponEntity findById(UUID id) {
        return null;
    }

    private UUID generateUniqueUUID() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("com.greenfoxacademy.springwebapp.weapon.models.weapons");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query;
        UUID id = null;
        boolean exists = false;
        while (!exists) {
            id = UUID.randomUUID();
            query = entityManager.createNativeQuery("SELECT EXISTS(SELECT id FROM weapons WHERE id = ?)");
            query.setParameter(1, id);
            exists = (boolean) query.getSingleResult();
        }
        return id;
    }

}
