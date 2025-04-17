package ru.otus.auth.service.service;

import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.auth.service.mapper.RegisterMapper;
import ru.otus.auth.service.model.dto.reg.RegRequestDto;
import ru.otus.auth.service.model.dto.reg.RegResponseDto;
import ru.otus.auth.service.repository.IdentifierRepository;
import ru.otus.auth.service.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegService {

    private final UserRepository userRepository;
    private final IdentifierRepository identifierRepository;
    private final RegisterMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegResponseDto register(RegRequestDto dto) {
        var email = dto.getEmail();
        var existingIdentifier = identifierRepository.findByLogin(email);
        if (existingIdentifier != null) {
            log.error("User with email {} already exists", email);
            throw new EntityExistsException("User with email '" + email + "' already exists");
        }

        var user = mapper.toUser(dto);
        var createdUser = userRepository.save(user);

        var secret = passwordEncoder.encode(dto.getPassword());

        var identifier = mapper.toIdentifier(createdUser.getId(), email, secret);
        identifierRepository.save(identifier);

        return new RegResponseDto(createdUser.getId());
    }
}
