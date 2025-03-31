package ru.otus.auth.service.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestDto {
    private UUID id;
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]*$")
    private String lastName;
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]*$")
    private String firstName;
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]*$")
    private String secondName;
    @NotEmpty
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@\" \n" +
            "        + \"[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$")
    private String email;
    @NotEmpty
    private UUID restaurantId;
}
