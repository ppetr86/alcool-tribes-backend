package com.greenfoxacademy.springwebapp.player.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfoxacademy.springwebapp.email.models.RegistrationTokenEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import lombok.*;

import javax.persistence.*;
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
  private String avatar;
  @Column(name = "points", columnDefinition = "integer default 0")
  private Integer points;

  @OneToOne(mappedBy = "player", cascade = CascadeType.ALL)
  private KingdomEntity kingdom;

  @NonNull
  private Boolean isAccountVerified;

  @OneToMany(mappedBy = "player")
  private Set<RegistrationTokenEntity> tokens;

  public PlayerEntity(String username, String password) {
    this.username = username;
    this.password = password;
  }

  @Override
  public String toString() {
    return "username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", email='" + email + '\'' +
            ", points=" + points;
  }
}