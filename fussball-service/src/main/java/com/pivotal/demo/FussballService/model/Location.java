package com.pivotal.demo.FussballService.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Location {
    @Id
    private Long id;
    public String city;
    public String country;
    public Float latitude;
    public Float longitude;
}
