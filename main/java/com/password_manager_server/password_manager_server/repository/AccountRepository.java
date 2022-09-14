package com.password_manager_server.password_manager_server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.password_manager_server.password_manager_server.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByName(String accoutName);

}
