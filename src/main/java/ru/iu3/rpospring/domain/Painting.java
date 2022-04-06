package ru.iu3.rpospring.domain;

import org.hibernate.annotations.Cascade;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Table
public class Painting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NonNull
    Integer id;

    @NonNull
    String name;

    @OneToOne
    @MapsId
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    Artist artistID;

    @ManyToOne
    @MapsId
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    Museum museumID;

    Integer year;

    public Painting() {

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

    public Artist getArtistID() {
        return artistID;
    }

    public void setArtistID(Artist artistID) {
        this.artistID = artistID;
    }

    public Museum getMuseumID() {
        return museumID;
    }

    public void setMuseumID(Museum museumID) {
        this.museumID = museumID;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
