package com.example.traveltrab_backend_mongo.DTOS;

import com.example.traveltrab_backend_mongo.entities.groups.Tasks;
import com.example.traveltrab_backend_mongo.entities.groups.enums.TypeGroup;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UpdateGroupRequestDTO {
    private String nameGroup; // Opcional
    private TypeGroup typeGroup; // Opcional
    private Date startDate; // Opcional
    private Date endDate; // Opcional
    private List<Tasks> tasks;
}
