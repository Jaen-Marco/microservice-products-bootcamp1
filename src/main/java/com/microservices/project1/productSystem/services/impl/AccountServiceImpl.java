package com.microservices.project1.productSystem.services.impl;

import com.microservices.project1.productSystem.models.Account;
import com.microservices.project1.productSystem.models.AccountType;
import com.microservices.project1.productSystem.repositories.AccountRepository;
import com.microservices.project1.productSystem.services.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private final AccountRepository accountRepository;

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
                existingAccount.setBalance(account.getBalance());
                existingAccount.getAccountType().setId(account.getAccountType().getId());
                existingAccount.getAccountType().setNameAccount(account.getAccountType().getNameAccount());
                existingAccount.getAccountType().setMovements(account.getAccountType().getMovements());
                return accountRepository.save(existingAccount);
            });
    }

    @Override
    public Mono<Void> deleteById(Long id) { return accountRepository.deleteById(id); }

    @Override
    public Flux<Account> findByClientId(Long clientId) { return accountRepository.findByClientId(clientId); }

    @Override
    public Mono<Double> debit(Long id, double amount) {
        var newAmount = validateMovements(id).block() ? amount + 20 : amount; // Cobrando comisión si hay más de 20 movimientos
        return accountRepository.findById(id)
                .map(updateAccountWithNewBalance(newAmount, Boolean.TRUE))
                .flatMap(accountRepository::save)
                .map(Account::getBalance);
    }

    @Override
    public Mono<Double> deposit(Long id, double amount) {
        var newAmount = validateMovements(id).block() ? amount - 20 : amount;
        return accountRepository.findById(id)
                .map(updateAccountWithNewBalance(newAmount, Boolean.FALSE))
                .flatMap(accountRepository::save)
                .map(Account::getBalance);
    }

    //Metodo para actualizar la cuenta con el nuevo saldo, para utilizar en los métodos debit y deposit
    private Function<Account, Account> updateAccountWithNewBalance(double amount, Boolean isDebit) {
        return accountToUpdate -> {
            var newBalance = calculateBalance(accountToUpdate.getBalance(), amount, isDebit);
            accountToUpdate.setBalance(newBalance);
            accountToUpdate.getAccountType().setMovements(accountToUpdate.getAccountType().getMovements() + 1);
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

    private Mono<Boolean> validateMovements(Long id) {
        return accountRepository.findById(id)
                .map(account -> account.getAccountType().getMovements() > 19)
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Void> resetMovements(Long clientId) {
        return accountRepository.findByClientId(clientId)
                .flatMap(account -> {
                    AccountType accountType = account.getAccountType();
                    accountType.setMovements(0L);
                    return accountRepository.save(account);
                })
                .then();
    }

}
