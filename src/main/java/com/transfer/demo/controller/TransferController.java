package com.transfer.demo.controller;

import com.transfer.demo.config.security.JwtUser;
import com.transfer.demo.dto.TransferResponseDto;
import com.transfer.demo.request.TransferRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/transfer/payment")
@Tag(name = "TransferController", description = "Контроллер для работы с переводами")
public interface TransferController {

    @PostMapping
    @Operation(summary = "Операция проведения платежа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Операция проведена успешно",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {TransferResponseDto.class}))),
            @ApiResponse(responseCode = "403", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    ResponseEntity<TransferResponseDto> transfer(@AuthenticationPrincipal JwtUser user,
                                                 @RequestBody @Valid TransferRequest request);

}