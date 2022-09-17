package com.password_manager_server.password_manager_server.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.password_manager_server.password_manager_server.model.Account;
import com.password_manager_server.password_manager_server.model.User;
import com.password_manager_server.password_manager_server.web.dto.UserRegistrationDto;

public interface UserService extends UserDetailsService {
    User save(UserRegistrationDto userRegristrationDto);

    User save(User user);

    User addAccount(String username, Account account);

    void updateAccount(Account acctToUpdate, String acctId);
}
