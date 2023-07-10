package com.microservices.project1.productSystem.repositories;

import com.microservices.project1.productSystem.models.Credit;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CreditRepository extends ReactiveCrudRepository<Credit, Long> {

    Flux<Credit> findByClientId(Long clientId);
}
