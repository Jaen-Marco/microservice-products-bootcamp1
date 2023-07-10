package com.microservices.project1.productSystem.controllers;

import com.microservices.project1.productSystem.models.Account;
import com.microservices.project1.productSystem.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public Flux<Account> findAllA() { return accountService.findAll(); }

    @GetMapping("/{id}")
    public Mono<Account> findByIdA(@PathVariable Long id) { return accountService.findById(id); }

    @PostMapping
    public Mono<Account> saveA(@RequestBody Account account) { return accountService.save(account); }

    @PutMapping("/{id}")
    public Mono<Account> updateA(@PathVariable Long id ,@RequestBody Account account) {
        return accountService.update(id, account);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteA(@PathVariable Long id) { return accountService.deleteById(id); }

    @GetMapping("/client/{clientId}")
    public Flux<Account> findByClientIdA(@PathVariable Long clientId) { return accountService.findByClientId(clientId); }

    @PutMapping("/{id}/debit={amount}")
    public Mono<Double> debitA(@PathVariable Long id, @PathVariable double amount) {
        return accountService.debit(id, amount);
    }

    @PutMapping("/{id}/deposit={amount}")
    public Mono<Double> depositA(@PathVariable Long id, @PathVariable double amount) {
        return accountService.deposit(id, amount);
    }
}
