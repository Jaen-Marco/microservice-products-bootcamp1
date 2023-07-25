package com.microservices.project1.productSystem.services.interf;

import com.microservices.project1.productSystem.models.DebitCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DebitCardService {
    Flux<DebitCard> findAll();
    Mono<DebitCard> findById(Long id);
    Mono<DebitCard> save(DebitCard debitCard);
    Mono<DebitCard> update(Long id, DebitCard debitCard);
    Mono<Void> deleteById (Long id);

    Mono<Double> debit(Long id, double amount);
    Mono<Double> deposit(Long id, double amount);
}
