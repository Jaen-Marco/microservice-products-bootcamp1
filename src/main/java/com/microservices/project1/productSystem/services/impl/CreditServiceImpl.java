package com.microservices.project1.productSystem.services.impl;

import com.microservices.project1.productSystem.models.Credit;
import com.microservices.project1.productSystem.repositories.CreditRepository;
import com.microservices.project1.productSystem.services.interf.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.stream.Stream;

@Service
public class CreditServiceImpl implements CreditService {

    @Autowired
    private final CreditRepository creditRepository;

    public CreditServiceImpl(CreditRepository creditRepository) {this.creditRepository = creditRepository; }

    @Override
    public Flux<Credit> findAll() { return creditRepository.findAll(); }

    @Override
    public Mono<Credit> findById(Long id) { return creditRepository.findById(id); }

    @Override
    public Mono<Credit> save(Credit credit) { return creditRepository.save(credit); }

    @Override
    public Mono<Credit> update(Long id, Credit credit) {
        return creditRepository.findById(id)
                .flatMap(existingCredit -> {
                    existingCredit.setClientId(credit.getClientId());
                    existingCredit.setBalance(credit.getBalance());
                    existingCredit.setCreditLine(credit.getCreditLine());
                    existingCredit.getCreditType().setId(credit.getCreditType().getId());
                    existingCredit.getCreditType().setNameCredit(credit.getCreditType().getNameCredit());
                    return creditRepository.save(existingCredit);
                });
    }

    @Override
    public Mono<Void> deleteById(Long id) { return creditRepository.deleteById(id); }

    @Override
    public Mono<Double> payCredit(Long id, double paymentAmount) {
        return creditRepository.findById(id)
                .map(updateBalanceForCredit(paymentAmount, Boolean.TRUE))
                .flatMap(creditRepository::save)
                .map(Credit::getBalance);
    }

    @Override
    public Mono<Double> useCredit(Long id, double purchaseAmount) {
        return creditRepository.findById(id)
                .map(updateBalanceForCredit(purchaseAmount, Boolean.FALSE))
                .flatMap(creditRepository::save)
                .map(Credit::getBalance);
    }

    @Override
    public Flux<Credit> findByClientId(Long clientId) { return creditRepository.findByClientId(clientId); }


    private Function<Credit, Credit> updateBalanceForCredit(double amount, boolean isPayment){
        return balanceToUpdate -> {
            var newBalance = calculateNewBalance(balanceToUpdate.getBalance(), balanceToUpdate.getCreditLine(), amount,isPayment);
            balanceToUpdate.setBalance(newBalance);
            return balanceToUpdate;
        };
    }

    private double calculateNewBalance(double balance, double creditLine, double amount, boolean isPayment) {
        double adjustedAmount = isPayment ? -amount : amount;
        return Stream.of(balance)
                .filter(b -> b != creditLine)
                .map(b -> b + adjustedAmount)
                .filter(b -> b >= 0 || b == creditLine)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("La operación excede los límites de la tarjeta."));
    }

}
