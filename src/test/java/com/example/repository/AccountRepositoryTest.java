package com.example.repository;

import com.example.demo.acoount.Account;
import com.example.demo.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AccountRepositoryTest {


    @Autowired
    private AccountRepository accountRepository;



    @Test
    void testing_findByEmail() {
        Account account = new Account();
        account.setEmail("mariana6666@gmail.com");
        account.setAccountHolderName("John Doe");
        accountRepository.save(account);

        Optional<Account> foundAccount = accountRepository.findByEmail("mariana6666@gmail.com");

        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get().getEmail()).isEqualTo("mariana6666@gmail.com");



    }

    @Test
    void testing_FindByEmail_NotFound(){

        Optional<Account> foundAccount = accountRepository.findByEmail("notEmail.example.com");

        assertThat(foundAccount).isNotPresent();



    }




}