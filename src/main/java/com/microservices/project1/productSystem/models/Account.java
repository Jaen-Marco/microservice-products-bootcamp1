package com.microservices.project1.productSystem.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id private Long id;
    private String type; //Ahorro, Cuenta Corriente y Plazo Fijo
    private Long clientId;
    private Double balance;

}
