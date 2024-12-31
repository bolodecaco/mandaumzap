package com.server.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.server.demo.dtos.BroadcastListDTO;
import com.server.demo.dtos.RequestBroadcastListDTO;
import com.server.demo.models.BroadcastList;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BroadcastListMapper {

    @Mapping(target = "owner", ignore = true)
    BroadcastListDTO toDTO(BroadcastList message);

    BroadcastList toEntity(RequestBroadcastListDTO message);

    List<BroadcastListDTO> toDTOList(List<BroadcastList> messages);
}
