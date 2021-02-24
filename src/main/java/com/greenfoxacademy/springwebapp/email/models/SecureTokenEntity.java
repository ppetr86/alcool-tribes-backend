package com.greenfoxacademy.springwebapp.configuration.email.models;

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
@Table(name = "secureTokens")
public class SecureTokenEntity {
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


  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Long getId() {
    return id;
  }

  public LocalDateTime getExpireAt() {
    return expireAt;
  }

  public void setExpireAt(LocalDateTime expireAt) {
    this.expireAt = expireAt;
  }

  public Timestamp getTimeStamp() {
    return timeStamp;
  }

  public boolean isExpired() {

    return getExpireAt().isBefore(LocalDateTime.now()); // this is generic implementation, you can always make it timezone specific
  }

  public PlayerEntity getPlayer() {
    return player;
  }

  public void setPlayer(PlayerEntity user) {
    this.player = user;
  }
}
