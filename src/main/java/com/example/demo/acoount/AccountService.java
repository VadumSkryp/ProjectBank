package com.example.demo.acoount;



import com.example.demo.repository.AccountRepository;
import com.example.demo.transaction.MyTransaction;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        account.setTransactions(new ArrayList<>());
        return accountRepository.save(account);

    }


    // Отримання ID
    @Cacheable("accountById")
    public Optional<Account> getAccount(Long id) {

        return accountRepository.findById(id);
    }


    // Добавлення коштів
    @Transactional
    @CacheEvict(value = "accountById", key = "#id")
    public Account deposit(Long id, double amount) {
        Account account = getAccount(id).orElseThrow(() -> new RuntimeException("Account not found "));
        account.setBalance(account.getBalance() + amount);


        MyTransaction myTransaction = new MyTransaction();
        myTransaction.setAccount(account);
        myTransaction.setAmount(amount);

        account.getTransactions().add(myTransaction);


        try {
            return accountRepository.save(account);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Failed to deposit amount, database operation failed", ex);
        }









    }


    //  Зняття коштів
    @Transactional
    @CacheEvict(value = "accountById", key = "#id")
    public Account withdraw(Long id, double amount) {
        Account account = getAccount(id).orElseThrow(() -> new RuntimeException("Account not found "));
        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds");
        }

        account.setBalance(account.getBalance() - amount);

        MyTransaction myTransaction = new MyTransaction();
        myTransaction.setAccount(account);
        myTransaction.setAmount(-amount);

        account.getTransactions().add(myTransaction);




        try {
           return accountRepository.save(account);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Failed to deposit amount, database operation failed", ex);
        }


    }


    // Переказ коштів
    @Transactional
    @CacheEvict(value = "accountById", allEntries = true)
    public void transfer(Long fromAccountId, Long toAccountId, double amount) {
        Account fromAccount = getAccount(fromAccountId).orElseThrow(() -> new RuntimeException("Sender account not found"));
        Account toAccount = getAccount(toAccountId).orElseThrow(() -> new RuntimeException("Receiver account not found"));


        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds");
        }


        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);





        MyTransaction fromTransaction = new MyTransaction();
        fromTransaction.setAccount(fromAccount);
        fromTransaction.setAmount(amount);

        fromAccount.getTransactions().add(fromTransaction);

        MyTransaction toTransaction = new MyTransaction();
        toTransaction.setAccount(toAccount);
        toTransaction.setAmount(amount);

        toAccount.getTransactions().add(toTransaction);











        try {
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Failed to deposit amount, database operation failed", ex);
        }
    }



    @Cacheable(value = "accountBalances", key = "#id")
    public double checkBalance(Long id) {
        Account account = getAccount(id).orElseThrow(() -> new RuntimeException("Account not found"));
        return account.getBalance();


    }


    @Cacheable(value = "AllAccounts")
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();

    }


    @Transactional
    @CacheEvict(value = "accountById", key = "#id")
    public void updateAccount(Long Id, String name, String email) {


        Account account = accountRepository.findById(Id)
                .orElseThrow(() -> new IllegalStateException(
                        "student with id " + Id + "does not exists"));


        if (name != null &&
                !name.isEmpty() &&
                !Objects.equals(account.getAccountHolderName(), name)) {
            account.setAccountHolderName(name);
        }


        if (email != null && !email.isEmpty() && !Objects.equals(account.getEmail(), email)) {
            Optional<Account> accountOptional = accountRepository.findByEmail(email);

            if (accountOptional.isPresent()) {
                throw new RuntimeException("email is taken");
            }

            account.setEmail(email);
        }

    }








}
