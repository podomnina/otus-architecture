package ru.otus.payment.service.mapper;

import org.mapstruct.Mapper;
import ru.otus.payment.service.model.dto.AccountResponseDto;
import ru.otus.payment.service.model.entity.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountResponseDto map(Account account);
}
