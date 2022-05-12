package com.kdt.springbootjpa.item.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Setter
@Getter
@Entity
@DiscriminatorValue("FURNITURE")
public class Furniture extends Item {
    private int height;
    private int width;
}
