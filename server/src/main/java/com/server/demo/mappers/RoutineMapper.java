package com.server.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.server.demo.dtos.RoutineDTO;
import com.server.demo.models.Routine;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoutineMapper {

    RoutineDTO toDTO(Routine routine);

    List<RoutineDTO> toDTOList(List<Routine> routines);
}
