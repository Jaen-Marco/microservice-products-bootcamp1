package com.microservices.project1.productSystem.controllers;

import com.microservices.project1.productSystem.models.Credit;
import com.microservices.project1.productSystem.services.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products/credits")
public class CreditController {

    @Autowired
    private CreditService creditService;

    @GetMapping
    public Flux<Credit> findAllC() { return creditService.findAll(); }

    @GetMapping("/{id}")
    public Mono<Credit> findByIdC(@PathVariable Long id) { return creditService.findById(id); }

    @GetMapping("/client/{clientId}")
    public Flux<Credit> findByClientIdC(@PathVariable Long clientId) { return creditService.findByClientId(clientId); }

    @PostMapping
    public Mono<Credit> saveC(@RequestBody Credit credit) { return creditService.save(credit); }

    @PutMapping("/{id}")
    public Mono<Credit> updateC(@PathVariable Long id ,@RequestBody Credit credit) {
        return creditService.update(id, credit);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteC(@PathVariable Long id) { return creditService.deleteById(id); }

    @PutMapping("/{id}/payment/amount={paymentAmount}")
    public Mono<Double> payCreditC(@PathVariable Long id, @PathVariable double paymentAmount){
        return creditService.payCredit(id, paymentAmount);
    }

    @PutMapping("/{id}/useCredit/amount={purchaseAmount}")
    public Mono<Double> useCreditC(@PathVariable Long id, @PathVariable double purchaseAmount){
        return creditService.useCredit(id, purchaseAmount);
    }
}
