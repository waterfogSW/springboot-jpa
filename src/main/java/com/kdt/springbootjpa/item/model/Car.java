package com.kdt.springbootjpa.item.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Setter
@Getter
@Entity
@DiscriminatorValue("CAR")
public class Car extends Item {
    private int power;
}
