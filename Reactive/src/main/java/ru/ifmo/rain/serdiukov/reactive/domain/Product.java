package ru.ifmo.rain.serdiukov.reactive.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true)
    public long id;
    @Column(nullable = false)
    public String name;
    @Column(nullable = false)
    public String description;
    @Column(nullable = false)
    public long priceRub;
}
