package com.transfer.demo.controller;

import com.transfer.demo.dto.TokenResponseDto;
import com.transfer.demo.request.EmailLoginRequest;
import com.transfer.demo.request.PhoneLoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/transfer/auth")
@Tag(name = "AuthController", description = "Контроллер для работы с авторизацией пользователя")
public interface AuthController {

    @PostMapping("/login/email")
    @Operation(summary = "Получение токена по email + пароль")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Токен получен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {TokenResponseDto.class}))),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    ResponseEntity<TokenResponseDto> loginByEmail(@RequestBody @Valid EmailLoginRequest request);

    @PostMapping("/login/phone")
    @Operation(summary = "Получение токена по номеру телефона + пароль")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Токен получен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {TokenResponseDto.class}))),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    ResponseEntity<TokenResponseDto> loginByPhone(@RequestBody @Valid PhoneLoginRequest request);

}