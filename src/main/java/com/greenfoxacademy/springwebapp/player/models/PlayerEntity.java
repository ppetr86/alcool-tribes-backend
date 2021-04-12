package com.greenfoxacademy.springwebapp.player.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfoxacademy.springwebapp.email.models.RegistrationTokenEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "players")
@Builder
public class PlayerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NonNull
  @Column(name = "username", unique = true, length = 20)
  private String username;

  @NonNull
  @Column(name = "password")
  @JsonIgnore
  private String password;

  @NonNull
  @Column(name = "email")
  private String email;
  @Column(name = "avatar")
  private String avatar = "genericName.jpg"; //this field is set to its actual default value within PlayerServiceImpl
  @Column(name = "points")
  private Integer points = 0; //TODO: need to have proper point logic

  @OneToOne(mappedBy = "player", cascade = CascadeType.ALL)
  private KingdomEntity kingdom;

  @NonNull
  private Boolean isAccountVerified;

  @NonNull
  @Enumerated(EnumType.STRING)
  private RoleType roleType;

  @OneToMany(mappedBy = "player")
  private Set<RegistrationTokenEntity> tokens;

  public PlayerEntity(String username, String password) {
    this.username = username;
    this.password = password;
  }

  @Override
  public String toString() {
    return "username='" + username + '\''
        + ", password='" + password + '\''
        + ", email='" + email + '\''
        + ", points=" + points;
  }
}

