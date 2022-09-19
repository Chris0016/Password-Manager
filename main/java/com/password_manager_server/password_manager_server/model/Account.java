package com.password_manager_server.password_manager_server.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Pattern(regexp = "^[a-zA-Z.\\-\\ ]*$", message = "Field only accepts, letters, .-")
    @Size(min = 2, max = 30)
    @Column(unique = true)
    private String name;

    /*
     * Because this password is for an external account we cannot enforce our
     * password policies on what makes a good password.
     * However, restrictions on invalid characters have been added.
     */
    @Pattern(regexp = "^[a-zA-Z0-9.\\-\\+=#?!@$%^&*_]*$", message = "Field only accepts, letters, +=#?!@$%^&*_")
    @Size(min = 2, max = 100)
    private String password;

    private LocalDate dateCreated;
    private LocalDate lastPasswordUpdate;

    public Account(String name, String password, LocalDate dateCreated, LocalDate lastPasswordUpdate) {
        this.name = name;
        this.password = password;
        this.dateCreated = dateCreated;
        this.lastPasswordUpdate = lastPasswordUpdate;
    }

    public Account() {
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

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getLastPasswordUpdate() {
        return lastPasswordUpdate;
    }

    public void setLastPasswordUpdate(LocalDate lastPasswordUpdate) {
        this.lastPasswordUpdate = lastPasswordUpdate;
    }

    public boolean equals(Account account) {
        return this.name.equals(
                account.getName()) && password.equals(account.getPassword());
    }
}