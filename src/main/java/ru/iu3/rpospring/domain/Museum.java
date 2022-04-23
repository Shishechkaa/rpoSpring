package ru.iu3.rpospring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
public class Museum {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    Long id;

    @Column(updatable = false, unique = true, nullable = false)
    String name;

    @Column(nullable = false)
    String location;

    @JsonIgnore
    @OneToMany
    public List<Painting> paintings = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "museum_usr", joinColumns = @JoinColumn(name = "museumID"),
            inverseJoinColumns = @JoinColumn(name = "usrID"))
    public Set<Usr> usrs = new HashSet<>();

    public Museum() {
    }

    public Museum(Long id) {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
