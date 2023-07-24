package com.microservices.project1.productSystem.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "credits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Credit {

    @Id private Long id;
    private Long clientId;
    private Double balance;
    private Double creditLine;
    private CreditType creditType;

}
