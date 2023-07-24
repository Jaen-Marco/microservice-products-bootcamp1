package com.microservices.project1.productSystem.services.interf;

import com.microservices.project1.productSystem.models.Credit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService {
    Flux<Credit> findAll();
    Mono<Credit> findById(Long id);
    Mono<Credit> save(Credit credit);
    Mono<Credit> update(Long id, Credit credit);
    Mono<Void> deleteById (Long id);
    Mono<Double> payCredit (Long id, double paymentAmount);
    Mono<Double> useCredit (Long id, double paymentAmount);
    Flux<Credit> findByClientId (Long clientId);
}
