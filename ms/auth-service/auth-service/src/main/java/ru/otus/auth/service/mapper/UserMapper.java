package ru.otus.auth.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.userdetails.UserDetails;
import ru.otus.auth.shared.model.AuthContext;
import ru.otus.auth.service.model.dto.auth.UserDto;
import ru.otus.auth.service.model.dto.user.CreateUserRequestDto;
import ru.otus.auth.service.model.dto.user.UserResponseDto;
import ru.otus.auth.service.model.entity.User;
import ru.otus.auth.shared.model.UserCtx;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "restaurant.id", target = "restaurantId")
    @Mapping(source = "restaurant.code", target = "restaurantCode")
    AuthContext toCtx(User user);

    @Mapping(target = "createdAt", expression = "java(java.time.OffsetDateTime.now())")
    @Mapping(target = "restaurant.id", source = "dto.restaurantId")
    User toEntity(CreateUserRequestDto dto);

    UserResponseDto toDto(User user);

    List<UserResponseDto> toDtos(List<User> users);

    UserDto toDto(UserCtx userCtx);

    UserCtx toUserCtx(AuthContext authContext);
}
