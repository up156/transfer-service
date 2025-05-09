package com.transfer.demo.service;

import com.transfer.demo.dto.TokenResponseDto;
import com.transfer.demo.request.EmailLoginRequest;
import com.transfer.demo.request.PhoneLoginRequest;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "AuthService", description = "Сервис для работы с авторизацией пользователя")
public interface AuthService {

    TokenResponseDto loginByEmail(EmailLoginRequest request);

    TokenResponseDto loginByPhone(PhoneLoginRequest request);
}