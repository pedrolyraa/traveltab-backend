package com.example.traveltrab_backend_mongo.DTOS;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateExpensesRequestDTO {
    private String description;
    private Float balance;
    private boolean isPaid;
}
