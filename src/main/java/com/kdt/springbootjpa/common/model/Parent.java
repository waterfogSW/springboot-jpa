package com.kdt.springbootjpa.common.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Parent {
    @EmbeddedId
    private ParentId id;
}
