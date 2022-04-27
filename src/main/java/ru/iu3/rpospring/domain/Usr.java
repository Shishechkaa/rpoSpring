package ru.iu3.rpospring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Usr {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    Long id;

    @Column(nullable = false, unique = true)
    String login;

    @JsonIgnore
    String password;

    @Column(nullable = false, unique = true)
    String email;

    @JsonIgnore
    String salt;

    String token;

    LocalDateTime activity;

    @ManyToMany(mappedBy = "usrs")
    public Set<Museum> museums = new HashSet<>();

    public Usr () {

    }

    public void addMuseum(Museum m) {
        this.museums.add(m);
        m.usrs.add(this);
    }

    public void removeMuseum(Museum m) {
        this.museums.remove(m);
        m.usrs.remove(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getActivity() {
        return activity;
    }

    public void setActivity(LocalDateTime activity) {
        this.activity = activity;
    }
}
