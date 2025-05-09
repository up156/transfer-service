package com.transfer.demo.controller.impl;

import com.transfer.demo.controller.AuthController;
import com.transfer.demo.dto.TokenResponseDto;
import com.transfer.demo.request.EmailLoginRequest;
import com.transfer.demo.request.PhoneLoginRequest;
import com.transfer.demo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    public ResponseEntity<TokenResponseDto> loginByEmail(@RequestBody @Valid EmailLoginRequest request) {
        return ResponseEntity.ok(authService.loginByEmail(request));
    }

    public ResponseEntity<TokenResponseDto> loginByPhone(@RequestBody @Valid PhoneLoginRequest request) {
        return ResponseEntity.ok(authService.loginByPhone(request));
    }
}
