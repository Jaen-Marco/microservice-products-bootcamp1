package com.microservices.project1.productSystem.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditType {

    private Long id;
    private String nameCredit; //Personal, Empresarial, Tarjeta de crédito
}
