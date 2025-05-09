package com.transfer.demo.controller;

import com.transfer.demo.config.security.JwtUser;
import com.transfer.demo.dto.UserDto;
import com.transfer.demo.request.UserDeleteContactsRequest;
import com.transfer.demo.request.UserUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/transfer/user")
@Tag(name = "UserController", description = "Контроллер для работы с аккаунтом пользователя")
public interface UserController {

    @GetMapping("/me")
    @Operation(summary = "Получение своего аккаунта текущим пользователем")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Аккаунт получен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {UserDto.class}))),
            @ApiResponse(responseCode = "403", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    ResponseEntity<UserDto> getMyProfile(@AuthenticationPrincipal JwtUser user);

    @PutMapping("/{me}")
    @Operation(summary = "Обновление аккаунта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Аккаунт обновлен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {UserDto.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    ResponseEntity<UserDto> update(@AuthenticationPrincipal JwtUser user, @RequestBody @Valid UserUpdateRequest request);

    @DeleteMapping("/contacts")
    @Operation(summary = "Удаление адреса почты или телефона")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Данные удалены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {Void.class}))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    ResponseEntity<Void> deleteContacts(@AuthenticationPrincipal JwtUser user, @RequestBody @Valid UserDeleteContactsRequest request);

    @GetMapping("/search")
    @Operation(summary = "Поиск аккаунта по переданным параметрам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Аккаунты получены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(allOf = {UserDto.class}))),
            @ApiResponse(responseCode = "403", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    ResponseEntity<Page<UserDto>> searchUsers(
            @RequestParam(required = false) @Size(min = 1, max = 500) String name,
            @RequestParam(required = false) @Email String email,
            @RequestParam(required = false) @Size(min = 11, max = 11) String phone,
            @RequestParam(required = false) @Past @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @PageableDefault(size = 20) Pageable pageable
    );

}