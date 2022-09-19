package com.password_manager_server.password_manager_server.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.password_manager_server.password_manager_server.model.Account;
import com.password_manager_server.password_manager_server.model.Role;
import com.password_manager_server.password_manager_server.model.User;
import com.password_manager_server.password_manager_server.repository.AccountRepository;
import com.password_manager_server.password_manager_server.repository.UserRepository;
import com.password_manager_server.password_manager_server.web.dto.UserRegistrationDto;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private AccountRepository accountRepository;

    @Lazy
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(@Lazy UserRepository userRepository, @Lazy AccountRepository accountRepository) {
        super();
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public User save(UserRegistrationDto userRegristrationDto) {
        User user = new User(userRegristrationDto.getFirstName(),
                userRegristrationDto.getLastName(),
                userRegristrationDto.getEmail(),
                passwordEncoder.encode(userRegristrationDto.getPassword()),
                Arrays.asList(new Role("ROLE_USER")),
                new ArrayList<>());

        return userRepository.save(user);
    }

    @Override
    public User addUser(User user) throws UsernameTakenException {

        userRepository.findByEmail(user.getEmail()).ifPresent(user1 -> {
            throw new UsernameTakenException();
        });

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid Username or Password"));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {

        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

    }

    @Override
    public User addAccount(String username, Account newAccount) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        LocalDate date = java.time.LocalDate.now();

        Account newService = new Account(
                newAccount.getName(),
                newAccount.getPassword(),
                date,
                date);

        user.getAcctList().add(newService);

        return userRepository.save(user);
    }

    @Override
    public void updateAccount(Account updatedAccount, String acctId)
            throws NoSuchElementException, AccountNameAlreadyExistsException {

        // Detect any changes

        Account acct = accountRepository.findById(Long.parseLong(acctId))
                .orElseThrow(() -> new NoSuchElementException("Account not found."));

        if (acct.equals(updatedAccount))// bad code fix me
            return; // No changes detected. Do nothing.

        // Check if changed name causes a collision
        if (!acct.getName().equals(updatedAccount.getName()))
            accountRepository.findByName(updatedAccount.getName()).ifPresent(foundAcct -> {
                throw new AccountNameAlreadyExistsException();
            });

        // Continue to update account otherwise
        acct.setName(updatedAccount.getName());
        acct.setPassword(updatedAccount.getPassword());
        acct.setLastPasswordUpdate(java.time.LocalDate.now());

        accountRepository.save(acct);

    }

    public class UsernameTakenException extends RuntimeException {

        public UsernameTakenException() {
            super("Username taken");
        }

        public UsernameTakenException(String message) {
            super(message);
        }
    }

    public class AccountNameAlreadyExistsException extends RuntimeException {

        public AccountNameAlreadyExistsException() {
            super("Accounnt name already taken.");
        }

        public AccountNameAlreadyExistsException(String message) {
            super(message);
        }
    }

}
