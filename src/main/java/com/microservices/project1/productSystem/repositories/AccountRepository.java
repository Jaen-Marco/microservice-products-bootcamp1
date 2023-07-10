package com.microservices.project1.productSystem.repositories;

import com.microservices.project1.productSystem.models.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.*;

@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account,Long> {

    Flux<Account> findByClientId(Long clientId);
}
