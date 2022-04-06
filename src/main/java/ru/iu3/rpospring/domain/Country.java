package ru.iu3.rpospring.domain;

import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Table
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NonNull
    Long id;

    @Column(unique = true)
    String name;

    public Country() {

    }

    public Country(Long id) {
        this.id = id;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
