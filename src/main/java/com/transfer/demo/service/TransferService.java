package com.transfer.demo.service;

import com.transfer.demo.config.security.JwtUser;
import com.transfer.demo.dto.TransferResponseDto;
import com.transfer.demo.request.TransferRequest;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "TransferService", description = "Сервис для работы с переводами пользователя")
public interface TransferService {

    TransferResponseDto transfer(JwtUser user, TransferRequest request);
}