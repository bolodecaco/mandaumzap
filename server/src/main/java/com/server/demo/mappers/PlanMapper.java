package com.server.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.server.demo.dtos.PlanDTO;
import com.server.demo.models.Plan;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlanMapper {

    PlanDTO toDTO(Plan plan);

    Plan toEntity(PlanDTO planDTO);

    List<PlanDTO> toDTOList(List<Plan> plans);
}
