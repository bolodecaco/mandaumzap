package com.server.demo.mappers;

import java.util.List;

import org.mapstruct.*;

import com.server.demo.dtos.BroadcastListDTO;
import com.server.demo.dtos.RequestBroadcastListDTO;
import com.server.demo.dtos.UpdateBroadcastListDTO;
import com.server.demo.models.BroadcastList;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ChatMapper.class})
public interface BroadcastListMapper {

    @Mapping(target = "ownerId", source = "owner.id")
    BroadcastListDTO toDTO(BroadcastList list);

    @Mapping(target = "owner.id", source = "ownerId")
    BroadcastList toEntity(RequestBroadcastListDTO list);

    List<BroadcastListDTO> toDTOList(List<BroadcastList> lists);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    BroadcastList updateEntityFromDTO(UpdateBroadcastListDTO dto, @MappingTarget BroadcastList list);
}
