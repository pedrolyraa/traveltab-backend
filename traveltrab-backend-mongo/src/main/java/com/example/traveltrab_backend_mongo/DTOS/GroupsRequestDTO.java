package com.example.traveltrab_backend_mongo.DTOS;


import com.example.traveltrab_backend_mongo.entities.groups.enums.TypeGroup;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class GroupsRequestDTO {
    private String nameGroup;
    private TypeGroup typeGroup;
    private Set<String> groupMembers;
    private Date startDate;
    private Date endDate;
}