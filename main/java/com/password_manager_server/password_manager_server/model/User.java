package com.password_manager_server.password_manager_server.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.password_manager_server.password_manager_server.config.AESEncryption;
import com.password_manager_server.password_manager_server.config.ValidPassword;

import lombok.Data;

@Entity
@Data
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Convert(converter = AESEncryption.class)
    @Pattern(regexp = "^[a-zA-Z.\\-\\ ]*$", message = "Field only accepts, letters, \".\",and \"-\"")
    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "first_name")
    private String firstName;

    @Convert(converter = AESEncryption.class)
    @Pattern(regexp = "^[a-zA-Z.\\-\\ ]*$", message = "Field only accepts, letters, \".\",and \"-\"")
    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "last_name")
    private String lastName;

    // uses variable name as column name if not assigned

    // @Pattern(regexp =
    // "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",
    // message = "Field only accepts, letters, numbers, @, _ , \".\", , and \"-\"")
    @NotNull
    @Size(min = 4, max = 50) // ANY CHANGES: MAKE SURE TO UPDATE IN THE CLIENT SIDE VALIDATION AS WELL
    @Email(message = "Please enter a valid email-address")
    private String email;

    // (regexp ="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$"
    @NotNull
    @ValidPassword // Max and min size validated by annotation as well as which characters in it.
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

    public Optional<Account> getAccount(String accountName) {
        for (Account a : acctList)
            if (a.getName().equals(accountName))
                return Optional.of(a);
        return Optional.empty();
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
