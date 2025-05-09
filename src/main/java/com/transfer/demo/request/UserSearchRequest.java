package com.transfer.demo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Schema(description = "Запрос на поиск пользователей")
public class UserSearchRequest {

    private String name;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
}
