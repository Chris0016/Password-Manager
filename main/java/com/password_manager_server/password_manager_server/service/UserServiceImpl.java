package com.password_manager_server.password_manager_server.service;

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
import com.password_manager_server.password_manager_server.repository.UserRepository;
import com.password_manager_server.password_manager_server.web.dto.AccountDto;
import com.password_manager_server.password_manager_server.web.dto.UserRegistrationDto;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Lazy
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(@Lazy UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
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

}
