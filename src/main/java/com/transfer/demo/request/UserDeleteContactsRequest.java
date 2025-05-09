package com.transfer.demo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "Запрос на удаление контактов из аккаунта")
public record UserDeleteContactsRequest(

        @Email
        String email,
        @Size(min = 11, max = 11)
        String phone
) {
}
