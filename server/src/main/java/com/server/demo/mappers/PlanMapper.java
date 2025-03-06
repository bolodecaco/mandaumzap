package com.server.demo.mappers;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.server.demo.dtos.PlanDTO;
import com.server.demo.dtos.RequestPlanDTO;
import com.server.demo.dtos.UpdatePlanDTO;
import com.server.demo.models.Plan;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlanMapper {

    PlanDTO toDTO(Plan plan);

    Plan toEntity(PlanDTO planDTO);

    Plan toEntityByRequestPlan(RequestPlanDTO requestPlanDTO);

    List<PlanDTO> toDTOList(List<Plan> plans);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Plan updateEntityFromDTO(UpdatePlanDTO dto, @MappingTarget Plan plan);
}
