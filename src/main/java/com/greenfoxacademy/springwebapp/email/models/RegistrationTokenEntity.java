package com.greenfoxacademy.springwebapp.email.models;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "registrationTokens")
public class RegistrationTokenEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String token;

  @CreationTimestamp
  @Column(updatable = false)
  private Timestamp timeStamp;

  @Column(updatable = false)
  @Basic(optional = false)
  private LocalDateTime expireAt;

  @ManyToOne
  @JoinColumn(name = "player_id", referencedColumnName = "id")
  private PlayerEntity player;

  private Boolean isExpired;

  public boolean isExpired() {
    return getExpireAt().isBefore(LocalDateTime.now());
  }
}
