package com.example.demo.acoount;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/accounts")
public class AccountController {


    @Autowired
    private AccountService accountService;

    @PostMapping
    public Account createAccount(@RequestBody Account account) {

        return accountService.createAccount(account);
    }

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable Long id) {
        return accountService.getAccount(id).orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @PostMapping("/{id}/deposit")
    public Account deposit(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        Double amount = request.get("amount");
        return accountService.deposit(id, amount);
    }

    @PostMapping("/{id}/withdraw")
    public Account withdraw(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        Double amount = request.get("amount");
        return accountService.withdraw(id, amount);
    }


    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam Long fromAccountId,
                                           @RequestParam Long toAccountId,
                                           @RequestParam double amount) {
        try {
            accountService.transfer(fromAccountId, toAccountId, amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/{id}/balance")
    public double checkBalance(@PathVariable Long id) {

        return accountService.checkBalance(id);
    }

    @GetMapping("/getAllAccount")
    public List<Account> getAllAccount() {
        return accountService.getAllAccounts();
    }


    @PutMapping("/{id}")
    public void updateStudent(@PathVariable("id") Long Id,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String email) {

        accountService.updateAccount(Id, name, email);
    }


}











