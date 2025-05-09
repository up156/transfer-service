package com.transfer.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Дто для пользователя")
public class UserDto {

    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private AccountDto account;
    @NotEmpty
    private List<String> emails;
    @NotEmpty
    private List<Long> phones;
}
