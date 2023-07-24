package com.microservices.project1.productSystem.repositories;

import com.microservices.project1.productSystem.models.DebitCard;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebitCardRepository extends ReactiveCrudRepository<DebitCard, Long> {

}
