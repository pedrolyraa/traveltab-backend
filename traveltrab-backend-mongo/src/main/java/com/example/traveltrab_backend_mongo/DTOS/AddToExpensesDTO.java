package com.example.traveltrab_backend_mongo.DTOS;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToExpensesDTO {
    private String userId;
    private Float valorInDebt;
}
