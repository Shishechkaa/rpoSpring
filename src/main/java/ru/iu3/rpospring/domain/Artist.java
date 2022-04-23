package ru.iu3.rpospring.domain;

import org.hibernate.annotations.Cascade;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Table
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    Long id;

    @Column(unique = true, nullable = false)
    String name;

    @ManyToOne
    @JoinColumn(name = "countryID")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    Country countryID;

    @Column(nullable = false)
    String century;

    public Artist() {

    }

    public Artist(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return countryID;
    }

    public void setCountry(Country countryID) {
        this.countryID = countryID;
    }

    public String getCentury() {
        return century;
    }

    public void setCentury(String century) {
        this.century = century;
    }
}
