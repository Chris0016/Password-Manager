package com.password_manager_server.password_manager_server.web.dto;

public class AccountDto {
    private String name;
    private String password;
    private String dateCreated;
    private String lastPasswordUpdate;

    public AccountDto(String name, String password, String dateCreated, String lastPasswordUpdate) {
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
