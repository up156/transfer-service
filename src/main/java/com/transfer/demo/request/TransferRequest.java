package com.transfer.demo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@Schema(description = "Запрос на перевод со счета пользователя")
public record TransferRequest(

        @Positive
        @NotNull
        Long toUserId,
        @Positive
        @NotNull
        BigDecimal amount
) {

}
