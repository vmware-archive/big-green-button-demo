package com.pivotal.demo.FussballService.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Team {
    @Id
    public Long id;
    public String name;
}
