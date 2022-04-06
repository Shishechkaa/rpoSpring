package ru.iu3.rpospring.domain;

import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Table
public class Museum {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NonNull
    Integer id;

    @NonNull
    String name;

    String location;

    public Museum() {

    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
