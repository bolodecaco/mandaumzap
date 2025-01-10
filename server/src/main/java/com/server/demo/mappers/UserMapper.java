package com.server.demo.mappers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.server.demo.dtos.RequestUserDTO;
import com.server.demo.dtos.UserDTO;
import com.server.demo.models.Session;
import com.server.demo.models.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "plan", ignore = true)
    @Mapping(target = "sessions", expression = "java(mapSessions(user.getSessions()))")
    UserDTO toDTO(User user);

    User toEntity(RequestUserDTO chat);

    List<UserDTO> toDTOList(List<User> users);

    default List<UUID> mapSessions(List<Session> sessions) {
        return sessions.stream()
                .map(Session::getId)
                .collect(Collectors.toList());
    }
}
