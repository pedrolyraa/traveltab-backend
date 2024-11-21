package com.example.traveltrab_backend_mongo.entities.groups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Tasks {
    private String name;
    private Date startDate;
    private Date endDate;
}
