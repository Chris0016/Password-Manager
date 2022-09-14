package com.password_manager_server.password_manager_server.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
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
import com.password_manager_server.password_manager_server.web.dto.AccountDto;
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
    public User addAccount(String username, AccountDto serviceDto) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        String date = java.time.LocalDate.now().toString();
        Account newService = new Account(
                serviceDto.getName(),
                serviceDto.getPassword(),
                date,
                date);

        user.getAcctList().add(newService);

        return userRepository.save(user);
    }

    @Override
    public void updateAccount(AccountDto accountDto, String oldAcctId) {

        // Detect any changes

        // Account acct = accountRepository.findByName(accountDto.getName())
        // .orElseThrow(() -> new NoSuchElementException("Account not found." +
        // accountDto));

        // Testing version: Delete After
        Account acct = accountRepository.findById(Long.parseLong(oldAcctId))
                .orElseThrow(() -> new NoSuchElementException("Account not found." + accountDto.getName()));
        // Testing version: Delete After

        if (acct.equals(accountDto.toAccount()))// bad code fix me
            return; // No changes detected. Do nothing.

        acct.setName(accountDto.getName());
        acct.setPassword(accountDto.getPassword());
        acct.setLastPasswordUpdate(java.time.LocalDate.now().toString());

        accountRepository.save(acct);

    }

}
