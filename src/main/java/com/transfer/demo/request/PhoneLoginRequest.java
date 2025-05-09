package com.transfer.demo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "Запрос на логин по телефону")
public record PhoneLoginRequest(

        @Size(min = 11, max = 11)
        String phone,

        @NotBlank
        String password
) {
}
