package com.microservices.project1.productSystem.services.impl;

import com.microservices.project1.productSystem.models.Account;
import com.microservices.project1.productSystem.repositories.AccountRepository;
import com.microservices.project1.productSystem.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.stream.Stream;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {this.accountRepository = accountRepository; }

    @Override
    public Flux<Account> findAll() { return accountRepository.findAll(); }

    @Override
    public Mono<Account> findById(Long id) { return accountRepository.findById(id); }

    @Override
    public Mono<Account> save(Account account) { return accountRepository.save(account); }

    @Override
    public Mono<Account> update(Long id, Account account) {
        return accountRepository.findById(id)
            .flatMap(existingAccount -> {
                existingAccount.setType(account.getType());
                existingAccount.setClientId(account.getClientId());
                existingAccount.setBalance(account.getBalance());
                return accountRepository.save(existingAccount);
            });
    }

    @Override
    public Mono<Void> deleteById(Long id) { return accountRepository.deleteById(id); }

    @Override
    public Flux<Account> findByClientId(Long clientId) { return accountRepository.findByClientId(clientId); }

    @Override
    public Mono<Double> debit(Long id, double amount) {
        return accountRepository.findById(id)
                .map(updateAccountWithNewBalance(amount, Boolean.TRUE))
                .flatMap(accountRepository::save)
                .map(Account::getBalance);
    }

    @Override
    public Mono<Double> deposit(Long id, double amount) {
        return accountRepository.findById(id)
                .map(updateAccountWithNewBalance(amount, Boolean.FALSE))
                .flatMap(accountRepository::save)
                .map(Account::getBalance);
    }

    //Metodo para actualizar la cuenta con el nuevo saldo, para utilizar en los métodos debit y deposit
    private Function<Account, Account> updateAccountWithNewBalance(double amount, Boolean isDebit) {
        return accountToUpdate -> {
            var newBalance = calculateBalance(accountToUpdate.getBalance(), amount, isDebit);
            accountToUpdate.setBalance(newBalance);
            return accountToUpdate;
        };
    }

    private double calculateBalance(double currentBalance, double amount, boolean isDebit) {
        return Stream.of(currentBalance)
                .filter(balance -> !isDebit || balance >= amount)
                .map(balance -> isDebit ? balance - amount : balance + amount)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Operación inválida o saldo insuficiente."));
    }

}
