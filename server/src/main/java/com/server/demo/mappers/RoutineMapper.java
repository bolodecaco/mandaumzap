package com.server.demo.mappers;

import com.server.demo.dtos.RoutineDTO;
import com.server.demo.models.Routine;
import org.springframework.stereotype.Service;

@Service
public class RoutineMapper {

    public RoutineDTO toDTO(Routine routine) {
        return new RoutineDTO(
            routine.getId(),
            routine.getOwner() != null ? routine.getOwner().getId() : null,
            routine.getTitle(),
            routine.getMessage() != null ? routine.getMessage().getId() : null,
            routine.getLastActiveAt(),
            routine.getWillActiveAt(),
            routine.getTimesSent()
        );
    }
}
