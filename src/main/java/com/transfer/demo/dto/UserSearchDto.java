package com.transfer.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Дто для ответа по поиску пользователей")
public class UserSearchDto {

    private List<UserDto> users;
    private int page;
    private int size;
    private long totalElements;
}
