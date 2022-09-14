package com.password_manager_server.password_manager_server.web.dto;

import java.time.LocalDate;

import com.password_manager_server.password_manager_server.model.Account;

public class AccountDto {
    private String name;
    private String password;
    private LocalDate dateCreated;
    private LocalDate lastPasswordUpdate;

    public AccountDto(String name, String password, LocalDate dateCreated, LocalDate lastPasswordUpdate) {
        super();
        this.name = name;
        this.password = password;
        this.dateCreated = dateCreated;
        this.lastPasswordUpdate = lastPasswordUpdate;
    }

    public AccountDto() {
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

    public boolean equals(AccountDto account) {
        return this.name.equals(
                account.getName()) && password.equals(account.getPassword());
    }

    public Account toAccount() {
        return new Account(this.getName(), this.getPassword(), this.getDateCreated(), this.getLastPasswordUpdate());
    }

}
