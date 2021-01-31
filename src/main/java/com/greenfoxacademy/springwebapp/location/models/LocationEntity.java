package com.greenfoxacademy.springwebapp.location.models;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "locations")
public class LocationEntity {

  // TODO: ALTB-14 - no generated ID as Kingdom and Player share the same ID
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private int x;
  private int y;
}
