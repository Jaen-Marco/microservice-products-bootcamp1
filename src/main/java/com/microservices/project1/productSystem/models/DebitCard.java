package com.microservices.project1.productSystem.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "debitCard")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DebitCard {

    @Id
    private Long id;
    private String name;
    private Long idClient;
    private List<Long> accountsId;
}
