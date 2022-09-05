package com.password_manager_server.password_manager_server.model;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String password;
    private String dateCreated;
    private String lastPasswordUpdate;

    public Service(String name, String password, String dateCreated, String lastPasswordUpdate) {
        this.name = name;
        this.password = password;
        this.dateCreated = dateCreated;
        this.lastPasswordUpdate = lastPasswordUpdate;
    }

    public Service() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLastPasswordUpdate() {
        return lastPasswordUpdate;
    }

    public void setLastPasswordUpdate(String lastPasswordUpdate) {
        this.lastPasswordUpdate = lastPasswordUpdate;
    }

}