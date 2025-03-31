package ru.otus.auth.gateway.mapper;

import org.mapstruct.Mapper;
import ru.otus.auth.gateway.model.UserDto;
import ru.otus.auth.service.api.model.AuthContext;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    UserDto toDto(AuthContext authContext);

}