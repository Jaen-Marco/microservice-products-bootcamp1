package com.microservices.project1.productSystem.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountType {

    private Long id;
    private String nameAccount; //Cuenta de Ahorros(1), Cuenta Corriente(2), Plazo Fijo(3), VIP(4), PYME(5)
    private Long movements;
}
