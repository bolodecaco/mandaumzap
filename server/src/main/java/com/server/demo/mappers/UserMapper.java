package com.server.demo.mappers;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.server.demo.dtos.RequestUserDTO;
import com.server.demo.dtos.UserDTO;
import com.server.demo.models.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "plan", source = "user.plan.name")
    UserDTO toDTO(User user);

    User toEntity(RequestUserDTO chat);

    List<UserDTO> toDTOList(List<User> users);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "plan", ignore = true)
    User updateEntityFromDTO(RequestUserDTO dto, @MappingTarget User user);
}
