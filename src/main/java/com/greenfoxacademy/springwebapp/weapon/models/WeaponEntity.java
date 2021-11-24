package com.greenfoxacademy.springwebapp.weapon.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorColumn(name = "discriminator",
        discriminatorType = DiscriminatorType.STRING)
@Table(name = "weapons")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AutomaticGunEntity.class, name = "AUTOMATIC_GUN"),
        @JsonSubTypes.Type(value = ColdWeaponEntity.class, name = "COLD_WEAPON"),
        @JsonSubTypes.Type(value = MachineGunEntity.class, name = "MACHINE_GUN"),
        @JsonSubTypes.Type(value = PistolEntity.class, name = "PISTOL"),
        @JsonSubTypes.Type(value = SemiAutomaticGunEntity.class, name = "SEMI_AUTOMATIC_GUN"),
        @JsonSubTypes.Type(value = ShotGunEntity.class, name = "SHOTGUN"),
        @JsonSubTypes.Type(value = SniperRifleEntity.class, name = "SNIPER_RIFLE"),
})
public abstract class WeaponEntity implements Serializable {
    @Id
    @Column(updatable = false, nullable = false)
    private UUID id;
    private String name;
    @Enumerated(EnumType.STRING)
    private WeaponType type;

    @ManyToOne
    @JoinColumn(name = "fk_kingdom")
    private KingdomEntity kingdom;

}
