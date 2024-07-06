package com.example.demo.repository;

import com.example.demo.acoount.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {


    @Query("select a from Account a where a.email = ?1")
    Optional<Account> findByEmail(String email);


}







