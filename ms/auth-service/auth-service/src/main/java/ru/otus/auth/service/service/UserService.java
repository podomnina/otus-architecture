package ru.otus.auth.service.service;

import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.otus.auth.service.mapper.UserMapper;
import ru.otus.auth.service.model.dto.CreateUserRequestDto;
import ru.otus.auth.service.model.dto.UpdateUserRequestDto;
import ru.otus.auth.service.model.dto.UserListResponseDto;
import ru.otus.auth.service.model.dto.UserResponseDto;
import ru.otus.auth.service.model.entity.User;
import ru.otus.auth.service.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    @Transactional
    public UserResponseDto create(CreateUserRequestDto dto) {
        //todo validate fields
        var existingUser = repository.findByLastNameAndFirstNameAndSecondNameAndEmailAndRestaurantId(
                dto.getLastName(), dto.getFirstName(), dto.getSecondName(),
                dto.getEmail(), dto.getRestaurantId());
        if (existingUser != null) {
            log.error("User with the same info already exists: {}", dto);
            throw new EntityExistsException("User already exists in the system"); //todo process exception
        }

        var user = mapper.toEntity(dto);
        var createdUser = repository.save(user);
        return mapper.toDto(createdUser);
    }

    @Transactional
    public UserResponseDto getById(UUID id) {
        var user = getUser(id);
        return mapper.toDto(user);
    }

    @Transactional
    public UserResponseDto update(UUID id, UpdateUserRequestDto dto) {
        var user = getUser(id);

        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }

        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }

        if (dto.getSecondName() != null) {
            user.setSecondName(dto.getSecondName());
        }

        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }

        repository.save(user);
        return mapper.toDto(user);
    }

    @Transactional
    public void delete(UUID id) {
        checkUserExists(id);
        repository.deleteById(id);
    }

    public UserListResponseDto getAll() {
        var users = repository.findAll(Sort.by("lastName", "firstName", "secondName"));
        var usersDto = mapper.toDtos(users);
        return new UserListResponseDto(usersDto);
    }

    private User getUser(UUID id) {
        var user = repository.findById(id);
        if (user.isEmpty()) {
            log.error("User with id {} not found", id);
            throw new NoSuchElementException("User with id " + id + " not found"); //todo process exception
        }

        return user.get();
    }

    private void checkUserExists(UUID id) {
        var userExists = repository.existsById(id);
        if (!userExists) {
            log.error("User with id {} not found", id);
            throw new NoSuchElementException("User with id " + id + " not found"); //todo process exception
        }
    }

}
