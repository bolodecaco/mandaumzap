package com.server.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.server.demo.dtos.RequestRoutineDTO;
import com.server.demo.dtos.RoutineDTO;
import com.server.demo.models.Routine;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoutineMapper {

    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "messageId", source = "message.id")
    RoutineDTO toDTO(Routine routine);

    @Mapping(target = "owner.id", source = "ownerId")
    @Mapping(target = "message.id", source = "messageId")
    Routine toEntity(RequestRoutineDTO routine);

    List<RoutineDTO> toDTOList(List<Routine> routines);
}
