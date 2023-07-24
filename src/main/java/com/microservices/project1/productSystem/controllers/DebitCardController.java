package com.microservices.project1.productSystem.controllers;

import com.microservices.project1.productSystem.models.DebitCard;
import com.microservices.project1.productSystem.services.interf.DebitCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products/debitCard")
public class DebitCardController {

    @Autowired
    private DebitCardService debitCardService;

    @GetMapping
    public Flux<DebitCard> findAllD() { return debitCardService.findAll(); }

    @GetMapping("/{id}")
    public Mono<DebitCard> findByIdD(@PathVariable Long id) { return debitCardService.findById(id); }

    @PostMapping
    public Mono<DebitCard> saveD(@RequestBody DebitCard debitCard) { return debitCardService.save(debitCard); }

    @PutMapping("/{id}")
    public Mono<DebitCard> updateD(@PathVariable Long id ,@RequestBody DebitCard debitCard) {
        return debitCardService.update(id, debitCard);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteD(@PathVariable Long id) { return debitCardService.deleteById(id); }


    @PutMapping("/{id}/debit={amount}")
    public Mono<Double> debitD(@PathVariable Long id, @PathVariable double amount){
        return debitCardService.debit(id, amount);
    }

}
