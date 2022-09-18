package com.password_manager_server.password_manager_server.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.password_manager_server.password_manager_server.config.ValidPassword;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(max = 15)
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Size(max = 15)
    @Column(name = "last_name")
    private String lastName;

    // uses variable name as column name if not assigned
    @NotNull
    @Size(max = 30)
    @Email(message = "Please enter a valid email-address")
    private String email;

    @NotNull
    @ValidPassword
    private String password;

    // Cascade type -> Whenever a change like those specified in CascaseType is
    // applied to the parent it will be applied to its child entities automatically
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Account> acctList = new ArrayList<>();

    public User(String firstName, String lastName, String email, String password, Collection<Role> roles,
            ArrayList<Account> acctList) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.acctList = acctList;
    }

    public User() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User [email=" + email + ", firstName=" + firstName + ", id=" + id + ", lastName=" + lastName
                + ", password=" + password + ", roles=" + roles + "]";
    }

    public List<Account> getAcctList() {
        return acctList;
    }

    public void setAcctList(List<Account> acctList) {
        this.acctList = acctList;
    }

    public void deleteAccountById(Long id) {
        for (Account account : acctList) {
            if (account.getId().equals(id)) {
                acctList.remove(account);
                return;
            }

        }
    }

    public void deleteAccountById(String id) {
        this.deleteAccountById(Long.parseLong(id));
    }
}
