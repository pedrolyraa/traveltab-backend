package com.example.traveltrab_backend_mongo.DTOS;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class ExpensesDTO {
    private String description;
    private Float balance;
    private Map<String, Float> assignedUsers;
    private Set<String> assignedGroups;
    private boolean splitEvenly;


}

