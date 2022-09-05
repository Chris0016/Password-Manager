package com.password_manager_server.password_manager_server.web.dto;

import java.util.ArrayList;
import java.util.List;

import com.password_manager_server.password_manager_server.model.Service;

public class UserRegistrationDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Service> serviceList = new ArrayList<>();

    public UserRegistrationDto(String firstName, String lastName, String email, String password,
            ArrayList<Service> serviceList) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.serviceList = serviceList;
    }

    public UserRegistrationDto() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Service> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
    }

}
