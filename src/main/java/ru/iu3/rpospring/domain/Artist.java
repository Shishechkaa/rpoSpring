package ru.iu3.rpospring.domain;

import org.hibernate.annotations.Cascade;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Table
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NonNull
    Integer id;

    @NonNull
    String name;

    @ManyToOne
    @MapsId
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    Country countryID;

    String age;

    public Artist() {

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

    public Country getCountry() {
        return countryID;
    }

    public void setCountry(Country countryID) {
        this.countryID = countryID;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
