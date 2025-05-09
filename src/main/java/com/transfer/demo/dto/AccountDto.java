package com.transfer.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Дто для аккаунта")
public class AccountDto {

    @Min(0)
    @NotNull
    private BigDecimal balance;

    @Min(0)
    @NotNull
    private BigDecimal initialDeposit;
}
