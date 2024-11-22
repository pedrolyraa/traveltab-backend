package com.example.traveltrab_backend_mongo.entities.groups;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Tasks {
    private String id = UUID.randomUUID().toString();
    private String name;
    private Date startDate;
    private Date endDate;
    private Boolean isDone;
}