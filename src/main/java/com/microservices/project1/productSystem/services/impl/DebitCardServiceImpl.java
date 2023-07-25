package com.microservices.project1.productSystem.services.impl;

import com.microservices.project1.productSystem.models.Account;
import com.microservices.project1.productSystem.models.DebitCard;
import com.microservices.project1.productSystem.repositories.AccountRepository;
import com.microservices.project1.productSystem.repositories.DebitCardRepository;
import com.microservices.project1.productSystem.services.interf.DebitCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DebitCardServiceImpl implements DebitCardService {

    @Autowired
    private final DebitCardRepository debitCardRepository;

    @Autowired
    private final AccountRepository accountRepository;

    @Override
    public Flux<DebitCard> findAll() { return debitCardRepository.findAll(); }

    @Override
    public Mono<DebitCard> findById(Long id) { return debitCardRepository.findById(id); }

    @Override
    public Mono<DebitCard> save(DebitCard debitCard) {return debitCardRepository.save(debitCard); }

    @Override
    public Mono<DebitCard> update(Long id, DebitCard debitCard) {
        return debitCardRepository.findById(id)
                .flatMap(existingDC -> {
                existingDC.setName(existingDC.getName());
                existingDC.setIdClient(existingDC.getIdClient());
                existingDC.setAccountsId(existingDC.getAccountsId());
                return debitCardRepository.save(existingDC);
                });
    }

    @Override
    public Mono<Void> deleteById(Long id) {return debitCardRepository.deleteById(id); }

    @Override
    public Mono<Double> debit(Long id, double amount){
        return debitCardRepository.findById(id)
                .flatMap(debitCard -> calculateNewBalance(debitCard.getAccountsId(), amount));
    }

    public Mono<Double> calculateNewBalance(List<Long> accountsId, double amount) {
        return Flux.fromIterable(accountsId)
                .flatMap(accountId -> accountRepository.findById(accountId))
                .filter(account -> account.getBalance() >= amount) // Filtrar las cuentas que cumplan con el monto
                .next()
                .flatMap(account -> {
                    double newBalance = account.getBalance() - amount;
                    account.setBalance(newBalance);
                    return accountRepository.save(account);
                })
                .map(Account::getBalance)
                .defaultIfEmpty(0.0); // Valor predeterminado si no hay cuentas válidas
    }

    @Override
    public Mono<Double> deposit(Long id, double amount) {
        return debitCardRepository.findById(id)
                .flatMap(debitCard -> {
                    List<Long> accountsId = debitCard.getAccountsId();
                    Long firstAccountId = accountsId.get(0);
                    return accountRepository.findById(firstAccountId)
                            .flatMap(account -> {
                                double newBalance = account.getBalance() + amount;
                                account.setBalance(newBalance);
                                return accountRepository.save(account)
                                        .map(Account::getBalance);
                            });
                });
    }
}
