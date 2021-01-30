package com.greenfoxacademy.springwebapp.location.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "locations")
@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class LocationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private int x;
  private int y;
}
