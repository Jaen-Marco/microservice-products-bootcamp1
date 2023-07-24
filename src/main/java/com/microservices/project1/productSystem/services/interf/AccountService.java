package com.microservices.project1.productSystem.services.interf;

import com.microservices.project1.productSystem.models.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.function.Function;

public interface AccountService {

    Flux<Account> findAll();
    Mono<Account> findById(Long id);
    Mono<Account> save(Account account);
    Mono<Account> update(Long id, Account account);
    Mono<Void> deleteById (Long id);
    Flux<Account> findByClientId (Long clientId);
    Mono<Double> debit (Long id, double amount);
    Mono<Double> deposit (Long id, double amount);
    Function<Account, Account> updateAccountWithNewBalance(double amount, Boolean isDebit);
    Mono<Void> resetMovements (Long id);
}
