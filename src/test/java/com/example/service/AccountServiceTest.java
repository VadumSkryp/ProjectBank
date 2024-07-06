package com.example.service;

import com.example.demo.acoount.Account;
import com.example.demo.repository.AccountRepository;
import com.example.demo.acoount.AccountService;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;


    @InjectMocks
    private AccountService accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        account = new Account();
        account.setId(1L);
        account.setBalance(1000.0);
        account.setAccountHolderName("Іван Іваненко");
        account.setEmail("ivan.ivanenko@example.com");






    }



    @Test
    void testCreateAccount() {
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account createdAccount = accountService.createAccount(account);

        assertNotNull(createdAccount);
        assertEquals(account.getId(), createdAccount.getId());
        assertEquals(account.getEmail(), createdAccount.getEmail());
        verify(accountRepository, times(1)).save(account);
    }



    @Test
    void testGetAccount() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Optional<Account> fetchedAccount = accountService.getAccount(1L);

        assertTrue(fetchedAccount.isPresent());
        assertEquals(account.getId(), fetchedAccount.get().getId());
    }


    @Test
    void testDeposit() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account updatedAccount = accountService.deposit(1L, 500.0);

        assertNotNull(updatedAccount);
        assertEquals(1500.0, updatedAccount.getBalance());
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).save(account);
    }



    @Test
    void testWithdraw() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account updatedAccount = accountService.withdraw(1L, 500.0);

        assertNotNull(updatedAccount);
        assertEquals(500.0, updatedAccount.getBalance());
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).save(account);
    }



    @Test
    void testWithdrawInsufficientFunds() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            accountService.withdraw(1L, 1500.0);
        });

        assertEquals("Insufficient funds", exception.getMessage());
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(0)).save(account);
    }



    @Test
    void testTransfer() {
        Account toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setBalance(500.0);
        toAccount.setAccountHolderName("Петро Петренко");
        toAccount.setEmail("petro.petrenko@example.com");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        accountService.transfer(1L, 2L, 500.0);

        assertEquals(500.0, account.getBalance());
        assertEquals(1000.0, toAccount.getBalance());
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).findById(2L);
        verify(accountRepository, times(1)).save(account);
        verify(accountRepository, times(1)).save(toAccount);
    }


    @Test
    void testCheckBalance() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        double balance = accountService.checkBalance(1L);

        assertEquals(1000.0, balance);
        verify(accountRepository, times(1)).findById(1L);
    }


    @Test
    void testGetAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);

        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> fetchedAccounts = accountService.getAllAccounts();

        assertNotNull(fetchedAccounts);
        assertEquals(1, fetchedAccounts.size());
        verify(accountRepository, times(1)).findAll();
    }








}