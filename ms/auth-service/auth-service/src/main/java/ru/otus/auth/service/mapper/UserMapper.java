package ru.otus.auth.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.auth.service.model.dto.CreateUserRequestDto;
import ru.otus.auth.service.model.dto.UserResponseDto;
import ru.otus.auth.service.model.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /*@Mapping(source = "restaurant.id", target = "restaurantId")
    @Mapping(source = "restaurant.code", target = "restaurantCode")
    AuthContext toCtx(User user);
*/
    @Mapping(target = "createdAt", expression = "java(java.time.OffsetDateTime.now())")
    @Mapping(target = "restaurant.id", source = "dto.restaurantId")
    User toEntity(CreateUserRequestDto dto);

    UserResponseDto toDto(User user);

    List<UserResponseDto> toDtos(List<User> users);
}
